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
					LOGGER.info("LMFAFMAKONJDGOADJINGOADNGHOADNHGOADNHOADHON");
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					System.out.print("curl 127.0.0.1:" + WEB_PORT);
					CUrl curl = new CUrl("curl http://localhost:" + WEB_PORT + "/index.html");
					curl.exec();
					break;
				} else {
					System.out.print(mcserveractive);
					try {
						TimeUnit.SECONDS.sleep(2);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}).start();
	}
}
