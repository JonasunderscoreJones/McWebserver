package me.jonasjones.mcwebserver;

import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.HTTPServer;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class McWebserver implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "mcwebserver";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Logger VERBOSELOGGER = LoggerFactory.getLogger(MOD_ID + " - VERBOSE LOGGER");

	@Override
	public void onInitialize() {

		// register configs
		ModConfigs.registerConfigs();

		LOGGER.info("McWebserver initialized!");

		if (ModConfigs.IS_ENABLED) {
			LOGGER.info("Starting Webserver...");

			new Thread(() -> {
				new HTTPServer(new Socket());
				HTTPServer.main();
			}).start();
		} else {
			LOGGER.info("Webserver disabled in the config file.");
		}
	}
}
