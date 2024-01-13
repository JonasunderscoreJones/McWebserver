package me.jonasjones.mcwebserver.web.php;

import me.jonasjones.mcwebserver.config.ModConfigs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class PhpServer {

    public static void main() {
        try {

            String documentRoot = ModConfigs.WEB_ROOT;

            // Set the port number
            int port = ModConfigs.WEB_PORT;

            // Build the command to start the PHP built-in web server
            String command = String.format("php -S localhost:%d -t %s", port, documentRoot);

            // Start the process
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println("PHP server is running. Press Enter to stop.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
