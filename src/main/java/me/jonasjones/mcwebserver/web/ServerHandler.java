package me.jonasjones.mcwebserver.web;

import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.php.PhpServer;
import net.fabricmc.loader.api.FabricLoader;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static me.jonasjones.mcwebserver.McWebserver.LOGGER;
import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_PORT;

public class ServerHandler implements Runnable {
    public static Socket socket = new Socket();

    private static final ServerHandler webserver = new ServerHandler();
    public static Thread webserverthread = new Thread(webserver);

    public static boolean mcserveractive = true;

    public static void start() {
        webserverthread.setName("McWebserver-webserver");
        webserverthread.start();
        // check if webserver is running
        // if not, stop the webserver
        // runs a loop while the webserver thread is alive
        // makes requests to the webserver until it dies (while the thread is listening for requests, it can't die and only checks if the server is running after a request)
        // truly a hacky and awful way to do this, but it works
        Thread serverthread = new Thread(() -> {
            while (webserverthread.isAlive()) {
                if (!mcserveractive) {
                    sleep(2);
                    try {
                        // Create URL
                        URL url = new URL("http://localhost:" + WEB_PORT + "/api/v1/dummy");

                        // Open connection
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        // Set request method
                        connection.setRequestMethod("GET");

                        // Set connection timeout (milliseconds)
                        connection.setConnectTimeout(1000);

                        // Set read timeout (milliseconds)
                        connection.setReadTimeout(1000);

                        // Get response code (optional, but useful for debugging)
                        int responseCode = connection.getResponseCode();
                        System.out.println("Response Code: " + responseCode);

                        // Close connection
                        connection.disconnect();
                    } catch (Exception ignored) {}
                    LOGGER.info("Webserver Stopped!");
                    break;
                } else {
                    sleep(2);
                }
            }
        });
        serverthread.setName("McWebserver-main");
        serverthread.start();
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        if ((ModConfigs.IS_ENABLED && !ModConfigs.PHP_ENABLED)) {
            LOGGER.info("Starting Webserver...");

            new HttpServer(socket);
            HttpServer.main();
        } else if (ModConfigs.PHP_ENABLED) {
            LOGGER.info("Starting php Webserver...");
            PhpServer.main();
        } else {
            LOGGER.info("Webserver disabled in the config file.");
        }
    }

    public static void createServerDir() {
        // create server dir as specified in the config WEB_ROOT
        Path path = FabricLoader.getInstance().getGameDir();
        Path webroot = path.resolve(ModConfigs.WEB_ROOT);
        webroot.toFile().mkdirs();
    }

    public static void createExampleFiles() {
        // create example files
        Path path = FabricLoader.getInstance().getGameDir();
        Path webroot = path.resolve(ModConfigs.WEB_ROOT);
        Path index = webroot.resolve(ModConfigs.WEB_FILE_ROOT);
        Path notfound = webroot.resolve(ModConfigs.WEB_FILE_404);
        index.toFile().mkdirs();
        notfound.toFile().mkdirs();
    }
}
