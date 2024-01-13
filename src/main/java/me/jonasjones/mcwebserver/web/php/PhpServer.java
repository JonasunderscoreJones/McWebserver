package me.jonasjones.mcwebserver.web.php;

import me.jonasjones.mcwebserver.McWebserver;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_PORT;
import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_ROOT;

public class PhpServer {
    private static Process process;

    public static void main() {
        try {

            process = getProcess();

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                McWebserver.VERBOSELOGGER.info(line);
            }

            McWebserver.LOGGER.info("PHP server started!");
        } catch (IOException e) {
            McWebserver.LOGGER.error("Error with PHP server: ", e);
        }
    }

    @NotNull
    private static Process getProcess() throws IOException {

        // Build the command to start the PHP built-in web server
        String command = String.format("php -S localhost:%d -t %s", WEB_PORT, WEB_ROOT);

        // Start the process
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        processBuilder.redirectErrorStream(true);
        return processBuilder.start();
    }

    public static void stop() {
        if (process == null || !process.isAlive()) {
            return;
        }
        process.destroy();
        McWebserver.LOGGER.info("PHP server stopped!");
    }
}
