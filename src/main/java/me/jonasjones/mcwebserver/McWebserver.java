package me.jonasjones.mcwebserver;

import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.api.v1.ApiHandler;
import me.jonasjones.mcwebserver.web.ServerHandler;
import me.jonasjones.mcwebserver.web.api.v2.tokenmgr.Token;
import me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static me.jonasjones.mcwebserver.commands.McWebCommand.registerCommands;
import static me.jonasjones.mcwebserver.config.ModConfigs.*;
import static me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenSaveManager.readOrCreateTokenFile;

public class McWebserver implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static String MOD_ID = "mcwebserver";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Logger VERBOSELOGGER = LoggerFactory.getLogger(MOD_ID + " - VERBOSE LOGGER");
	public static Boolean ISFIRSTSTART = false;
	public static MinecraftServer MC_SERVER;

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
			if (API_INGAME_COMMAND_ENABLED) {
				ArrayList< Token > tokens = readOrCreateTokenFile();
				LOGGER.info("Loaded " + tokens.size() + " tokens from file.");
				// register commands
				registerCommands();
			}


			//start collecting api info
			ApiHandler.startHandler();
			LOGGER.info("Server API enabled!");
			/*if (ADV_API_ENABLED) {
				//start collecting advanced api info
				ApiHandler.startAdvHandler();
				LOGGER.info("Advanced Server API enabled!");
			} else {
				LOGGER.info("Advanced Server API disabled in the config file.");
			}*/
		} else {
			LOGGER.info("Server API disabled in the config file.");
		}

		ServerHandler.start();
	}
}
