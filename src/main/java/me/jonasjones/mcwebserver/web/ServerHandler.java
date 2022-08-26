package me.jonasjones.mcwebserver.web;


import me.jonasjones.mcwebserver.McWebserver;
import net.minecraft.server.MinecraftServer;

public class ServerHandler extends Thread {
    static ServerHandler thread = new ServerHandler();
    public static void startServer() {
        McWebserver.LOGGER.info("Starting Webserver...");
        thread.start();
    }

    public static void stopServer() throws InterruptedException {
        McWebserver.LOGGER.info("Stopping Webserver...");
        thread.interrupt();
        McWebserver.LOGGER.info("Webserver stopped!");
    }

    public void run() {
        HTTPServer.main();
    }
}
