package me.jonasjones.mcwebserver.web;


import me.jonasjones.mcwebserver.config.ModConfigs;

import java.net.Socket;

import static me.jonasjones.mcwebserver.McWebserver.LOGGER;

public class ServerHandler implements Runnable {
    public static Socket socket = new Socket();

    public void run() {
        if (ModConfigs.IS_ENABLED) {
            LOGGER.info("Starting Webserver...");

            new HTTPServer(socket);
            HTTPServer.main();
        } else {
            LOGGER.info("Webserver disabled in the config file.");
        }
    }
}
