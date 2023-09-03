package me.jonasjones.mcwebserver;

import com.roxstudio.utils.CUrl;
import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.ServerHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_PORT;

public class McWebserver implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "mcwebserver";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Logger VERBOSELOGGER = LoggerFactory.getLogger(MOD_ID + " - VERBOSE LOGGER");
	private static ServerHandler webserver = new ServerHandler();
	public static Thread webserverthread = new Thread(webserver);
	public static boolean mcserveractive = true;

	@Override
	public void onInitialize() {

		// register configs
		ModConfigs.registerConfigs();

		LOGGER.info("McWebserver initialized!");


	webserverthread.start();
		new Thread(() -> {
			while (true) {
				if (!mcserveractive) {
					sleep(2);
					for (int i = 0; i < 2; i++) {
						CUrl curl = new CUrl("http://localhost:" + WEB_PORT + "/index.html").timeout(1, 1);
						curl.exec();
						sleep(1);
					}
					LOGGER.info("Webserver Stopped!");
					break;
				} else {
					sleep(2);
				}
			}
		}).start();
	}

	private void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
