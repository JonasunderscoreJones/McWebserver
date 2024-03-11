package me.jonasjones.mcwebserver.web;

import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.McWebserver;
import me.jonasjones.mcwebserver.util.VerboseLogger;
import me.jonasjones.mcwebserver.web.api.ErrorHandler;
import me.jonasjones.mcwebserver.web.api.v1.ApiV1Handler;
import me.jonasjones.mcwebserver.web.api.ApiRequests;
import me.jonasjones.mcwebserver.web.api.ApiRequestsUtil;
import me.jonasjones.mcwebserver.web.api.v2.ApiV2Handler;
import me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenManager;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.StringTokenizer;

import static me.jonasjones.mcwebserver.web.ServerHandler.mcserveractive;
import static me.jonasjones.mcwebserver.web.api.v1.ApiV1Handler.isApiV1Request;
import static me.jonasjones.mcwebserver.web.api.v2.ApiV2Handler.isApiV2Request;

public class HttpServer implements Runnable {
    static Path WEB_ROOT;
    static final String DEFAULT_FILE = ModConfigs.WEB_FILE_ROOT;
    static final String FILE_NOT_FOUND = ModConfigs.WEB_FILE_404;
    static final String METHOD_NOT_SUPPORTED = ModConfigs.WEB_FILE_NOSUPPORT;
    // port to listen connection
    static final int PORT = ModConfigs.WEB_PORT;

    private static final byte[] NOT_IMPLEMENTED = "HTTP/1.1 405 Method Not Allowed\r\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] OK = "HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] NOT_FOUND = "HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] HEADERS = String.join(
            "\r\n",
            "Server: Java HTTP Server from SSaurel : 1.0",
            "X-Frame-Options: DENY",
            "X-Content-Type-Options: nosniff",
            "" // trailing CRLF
    ).getBytes(StandardCharsets.UTF_8);
    private static final byte[] CRLF = new byte[]{0x0D, 0x0A};

    // Client Connection via Socket Class
    private final Socket connect;
    private final MimeTypeIdentifier mimetypeidentifier = new MimeTypeIdentifier();
    private Boolean isApiv1Request = false;
    private Boolean isApiv2Request = false;

    static {
        try {
            WEB_ROOT = Path.of(ModConfigs.WEB_ROOT).toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException e) {
            WEB_ROOT = Path.of(ModConfigs.WEB_ROOT);
        }
    }

    public HttpServer(Socket c) {
        connect = c;
    }

    public static void main() {
        try {
            try (ServerSocket serverConnect = new ServerSocket(PORT)) {
                McWebserver.LOGGER.info("Server started.");
                McWebserver.LOGGER.info("Listening for connections on port : " + PORT);

                // we listen until user halts server execution
                while (mcserveractive) {
                    HttpServer myServer = new HttpServer(serverConnect.accept());

                    VerboseLogger.info("Connection opened. (" + Instant.now() + ")");

                    // create dedicated thread to manage the client connection
                    Thread thread = new Thread(myServer);
                    thread.setName("McWebserver-worker");
                    thread.start();

                }
            }

        } catch (IOException e) {
            VerboseLogger.error("Server Connection error : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        if (mcserveractive) {
            // we manage our particular client connection
            BufferedReader in = null;
            PrintWriter out = null;
            BufferedOutputStream dataOut = null;
            String fileRequested = null;
            String apiToken = null;

            try {
                // we read characters from the client via input stream on the socket
                in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                // we get character output stream to client (for headers)
                out = new PrintWriter(connect.getOutputStream());
                // get binary output stream to client (for requested data)
                dataOut = new BufferedOutputStream(connect.getOutputStream());

                // get first line of the request from the client
                String input = in.readLine();
                // we parse the request with a string tokenizer
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                // we get file requested
                fileRequested = parse.nextToken();
                String header;
                while ((header = in.readLine()) != null && !header.isEmpty()) {

                    // Check if the header contains your API token
                    if (header.startsWith("Authorization: Bearer ")) {
                        apiToken = header.substring("Authorization: Bearer ".length());
                    }
                }

                // we support only GET and HEAD methods, we check
                if (!method.equals("GET") && !method.equals("HEAD")) {
                    isApiv1Request = false;
                    VerboseLogger.info("501 Not Implemented : " + method + " method.");

                    // we return the not supported file to the client
                    Path file = WEB_ROOT.resolve(METHOD_NOT_SUPPORTED);
                    long fileLength = Files.size(file);
                    String contentMimeType = "text/html";
                    //read content to return to client
                    byte[] fileData = readFileData(file);

                    // we send HTTP Headers with data to client
                    dataOut.write(NOT_IMPLEMENTED);
                    dataOut.write(HEADERS); //hopefully enough credits
                    dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                    dataOut.write("Content-Type: %s\r\n".formatted(contentMimeType).getBytes(StandardCharsets.UTF_8));
                    dataOut.write("Content-Length: %s\r\n".formatted(fileLength).getBytes(StandardCharsets.UTF_8));
                    dataOut.write(CRLF); // blank line between headers and content, very important !
                    // file
                    dataOut.write(fileData, 0, fileData.length);
                    dataOut.flush();
                } else if (isApiV1Request(fileRequested)) {
                    isApiv1Request = true;
                    // Check if server API is enabled
                    if (ModConfigs.SERVER_API_ENABLED) {
                        // Set appropriate response headers
                        dataOut.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                        if (fileRequested.equals("/api/v1/servericon")) {
                            dataOut.write("Content-Type: image/png\r\n".getBytes(StandardCharsets.UTF_8));
                            // Get server icon from ApiHandler
                            byte[] serverIcon = ApiRequestsUtil.getServerIcon();
                            int contentLength = serverIcon.length;

                            dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                            dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content

                            // Send server icon
                            dataOut.write(serverIcon, 0, contentLength);
                            dataOut.flush();
                        } else {
                            dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
                            String jsonString = "";
                            try {
                                // Get JSON data from ApiHandler
                                jsonString = ApiV1Handler.handle(fileRequested);
                            } catch (Exception e) {
                                VerboseLogger.error("Error getting JSON data from ApiHandler: " + e.getMessage());
                                jsonString = ErrorHandler.internalServerErrorString();
                            }


                            byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
                            int contentLength = jsonBytes.length;

                            dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                            dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content

                            // Send JSON data
                            dataOut.write(jsonBytes, 0, contentLength);
                            dataOut.flush();
                        }
                    } else {
                        // Server API is disabled
                        String jsonString = ErrorHandler.forbiddenRequestString();
                        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
                        int contentLength = jsonBytes.length;
                        dataOut.write("HTTP/1.1 403 Forbidden\r\n".getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
                        dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                        dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content
                        dataOut.write(jsonBytes, 0, contentLength);
                        dataOut.flush();
                    }
                } else if (isApiV2Request(fileRequested)) {
                    isApiv1Request = false;
                    // Check if server API is enabled
                    if (ModConfigs.SERVER_API_ENABLED) {
                        dataOut.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
                        String jsonString = "";
                        try {
                            // Get JSON data from ApiHandler
                            jsonString = ApiV2Handler.handle(fileRequested, apiToken);
                        } catch (Exception e) {
                            VerboseLogger.error("Error getting JSON data from ApiHandler: " + e.getMessage());
                            jsonString = ErrorHandler.internalServerErrorString();
                        }

                        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
                        int contentLength = jsonBytes.length;

                        dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                        dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content

                        // Send JSON data
                        dataOut.write(jsonBytes, 0, contentLength);
                        dataOut.flush();
                    }
                } else {
                    isApiv1Request = false;
                    // GET or HEAD method
                    if (fileRequested.endsWith("/")) {
                        fileRequested += DEFAULT_FILE;
                    }
                    if (fileRequested.startsWith("/")) {
                        fileRequested = fileRequested.substring(1);
                    }

                    try {
                        boolean isAuthorized = false;
                        if (!ModConfigs.WEB_REQUIRE_TOKEN) {
                            isAuthorized = true;
                        } else if (apiToken != null) {
                            isAuthorized = TokenManager.isTokenValid(apiToken);
                        }

                        if (isAuthorized) {

                            Path file = WEB_ROOT.resolve(fileRequested).toRealPath(LinkOption.NOFOLLOW_LINKS);
                            if (!file.startsWith(WEB_ROOT)) {
                                VerboseLogger.warn("Access to file outside root: " + file);
                                throw new NoSuchFileException(fileRequested);
                            }
                            int fileLength = (int) Files.size(file);
                            int fileExtensionStartIndex = fileRequested.lastIndexOf(".") + 1;
                            String contentType;
                            if (fileExtensionStartIndex > 0) {
                                contentType = mimetypeidentifier.compare(fileRequested.substring(fileExtensionStartIndex));
                            } else {
                                contentType = "text/plain";
                            }

                            byte[] fileData = readFileData(file);

                            // send HTTP Headers
                            dataOut.write(OK);
                            dataOut.write(HEADERS);
                            dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                            dataOut.write("Content-Type: %s\r\n".formatted(contentType).getBytes(StandardCharsets.UTF_8));
                            dataOut.write("Content-Length: %s\r\n".formatted(fileLength).getBytes(StandardCharsets.UTF_8));
                            dataOut.write(CRLF); // blank line between headers and content, very important !
                            if (method.equals("GET")) { // GET method so we return content
                                dataOut.write(fileData, 0, fileLength);
                                dataOut.flush();
                            }

                            VerboseLogger.info("File " + fileRequested + " of type " + contentType + " returned");
                        } else {
                            dataOut.write("HTTP/1.1 200 OK\r\n".getBytes(StandardCharsets.UTF_8));
                            dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                            dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
                            String jsonString = ErrorHandler.unauthorizedString();


                            byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
                            int contentLength = jsonBytes.length;

                            dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                            dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content

                            // Send JSON data
                            dataOut.write(jsonBytes, 0, contentLength);
                            dataOut.flush();
                        }

                    } catch (NoSuchAlgorithmException e) {
                        McWebserver.LOGGER.error("Error getting JSON data from ApiHandler: " + e.getMessage());
                    }



                }

            } catch (NoSuchFileException e) {
                try {
                    assert out != null;
                    assert dataOut != null;
                    VerboseLogger.error("Error with file not found exception : " + e.getMessage());
                    fileNotFound(out, dataOut, fileRequested);
                } catch (IOException ioe) {
                    // send json 404 error
                    // replace mime type header with application/json
                    try {
                        dataOut = new BufferedOutputStream(connect.getOutputStream());
                        dataOut.write("HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
                        dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
                        String jsonString = ErrorHandler.internalServerErrorString();
                        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
                        int contentLength = jsonBytes.length;
                        dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
                        dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content
                        dataOut.write(jsonBytes, 0, contentLength);
                        dataOut.flush();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            } catch (IOException ioe) {
                VerboseLogger.error("Server error : " + ioe);
            } finally {
                try {
                    in.close();
                    out.close();
                    assert dataOut != null;
                    dataOut.close();
                    connect.close(); // we close socket connection
                } catch (Exception e) {
                    VerboseLogger.error("Error closing stream : " + e.getMessage());
                }

                VerboseLogger.info("Connection closed.");
            }

        }
    }

    /*private BufferedOutputStream responseConstructor(BufferedOutputStream dataOut, String content, String mimeType) {
        dataOut.write()
    }*/

    private byte[] readFileData(Path file) throws IOException {
        return Files.readAllBytes(file);
    }

    private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
        try {
            Path file = WEB_ROOT.resolve(FILE_NOT_FOUND);
            int fileLength = (int) Files.size(file);
            String contentType = "text/html";
            byte[] fileData = readFileData(file);

            dataOut.write(NOT_FOUND);
            dataOut.write(HEADERS);
            dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
            dataOut.write("Content-Type: %s\r\n".formatted(contentType).getBytes(StandardCharsets.UTF_8));
            dataOut.write("Content-Length: %s\r\n".formatted(fileLength).getBytes(StandardCharsets.UTF_8));
            dataOut.write(CRLF); // blank line between headers and content, very important !
            out.flush(); // flush character output stream buffer

            dataOut.write(fileData, 0, fileLength);
            dataOut.flush();

            VerboseLogger.error("File " + fileRequested + " not found");
        } catch (Exception e) {
            VerboseLogger.error("Error with file not found exception : " + e.getMessage());
            dataOut.write("HTTP/1.1 404 Not Found\r\n".getBytes(StandardCharsets.UTF_8));
            dataOut.write("Date: %s\r\n".formatted(Instant.now()).getBytes(StandardCharsets.UTF_8));
            dataOut.write("Content-Type: application/json\r\n".getBytes(StandardCharsets.UTF_8));
            String jsonString = ErrorHandler.notFoundErrorString();
            byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
            int contentLength = jsonBytes.length;
            dataOut.write(("Content-Length: " + contentLength + "\r\n").getBytes(StandardCharsets.UTF_8));
            dataOut.write("\r\n".getBytes(StandardCharsets.UTF_8)); // Blank line before content
            dataOut.write(jsonBytes, 0, contentLength);
            dataOut.flush();

            VerboseLogger.error("File " + fileRequested + " not found");
        }
    }
}
