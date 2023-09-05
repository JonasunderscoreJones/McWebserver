package me.jonasjones.mcwebserver.web;


import com.roxstudio.utils.CUrl;
import me.jonasjones.mcwebserver.config.ModConfigs;

import java.net.Socket;
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
        Thread serverthread = new Thread(() -> {
            while (true) {
                if (!mcserveractive) {
                    sleep(2);
                    for (int i = 0; i < 2; i++) {
                        CUrl curl = new CUrl("http://localhost:" + WEB_PORT + "/api/v1/dummy").timeout(1, 1); // a truly awful way of stopping this thread
                        curl.exec();
                        sleep(1);
                    }
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
        if (ModConfigs.IS_ENABLED) {
            LOGGER.info("Starting Webserver...");

            new HttpServer(socket);
            HttpServer.main();
        } else {
            LOGGER.info("Webserver disabled in the config file.");
        }
    }
}
