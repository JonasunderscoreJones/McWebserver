package me.jonasjones.mcwebserver;

import com.roxstudio.utils.CUrl;
import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.HttpServer;
import me.jonasjones.mcwebserver.web.api.v1.ApiHandler;
import me.jonasjones.mcwebserver.web.ServerHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static me.jonasjones.mcwebserver.config.ModConfigs.*;

public class McWebserver implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "mcwebserver";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Logger VERBOSELOGGER = LoggerFactory.getLogger(MOD_ID + " - VERBOSE LOGGER");
	public static Boolean ISFIRSTSTART = false;

	@Override
	public void onInitialize() {

		// register configs
		ModConfigs.registerConfigs();
		LOGGER.info("McWebserver initialized!");

		if (ISFIRSTSTART) {
			LOGGER.info("");
			ServerHandler.createServerDir();

		}

		if (SERVER_API_ENABLED) {
			//start collecting api info
			ApiHandler.startHandler();
			LOGGER.info("Server API enabled!");
			if (ADV_API_ENABLED) {
				//start collecting advanced api info
				ApiHandler.startAdvHandler();
				LOGGER.info("Advanced Server API enabled!");
			} else {
				LOGGER.info("Advanced Server API disabled in the config file.");
			}
		} else {
			LOGGER.info("Server API disabled in the config file.");
		}

		ServerHandler.start();
	}
}
