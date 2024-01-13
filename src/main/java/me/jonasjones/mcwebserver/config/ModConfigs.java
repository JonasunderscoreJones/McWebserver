package me.jonasjones.mcwebserver.config;

import com.mojang.datafixers.util.Pair;

import me.jonasjones.mcwebserver.McWebserver;
import me.jonasjones.mcwebserver.util.VerboseLogger;

public class ModConfigs {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider config;

    //config
    public static Boolean IS_ENABLED;
    public static int WEB_PORT;
    public static String WEB_ROOT;
    public static String WEB_FILE_ROOT;
    public static String WEB_FILE_404;
    public static Boolean SERVER_API_ENABLED;
    public static Boolean ADV_API_ENABLED;
    public static Boolean API_INGAME_COMMAND_ENABLED;
    public static String WEB_FILE_NOSUPPORT;

    public static Boolean PHP_ENABLED;
    public static Boolean VERBOSE = false; //needs to be set to false since the verbose logger is called before config file is fully loaded


    public static void registerConfigs() {

        config = new ModConfigProvider();

        createConfigs();

        CONFIG = SimpleConfig.of(McWebserver.MOD_ID).provider(config).request();

        assignConfigs();

        //make verbose logger show that it is active and print configs to logger
        VerboseLogger.info("Verbose Logger is now logging.");
        VerboseLogger.info("Loaded McWebserver config file successfully: found " + config.getConfigsList().size() + " overrides and configurations.");
    }

    private static void createConfigs() {
        config.addKeyValuePair(new Pair<>("web.isEnabled", false), "whether or not the webserver should be enabled or not");
        config.addKeyValuePair(new Pair<>("web.port", 8080), "The port of the webserver");
        config.addKeyValuePair(new Pair<>("web.root", "webserver/"), "the root directory of the webserver, starting from the main server directory");
        config.addKeyValuePair(new Pair<>("web.file.root", "index.html"), "the name of the html file for the homepage");
        config.addKeyValuePair(new Pair<>("web.file.404", "404.html"), "the name of the html file for 404 page");
        config.addKeyValuePair(new Pair<>("web.api", true), "whether or not the webserver api should be enabled or not");
        config.addKeyValuePair(new Pair<>("web.api.adv", true), "whether or not the api should expose information such as player coordinates and inventory");
        config.addKeyValuePair(new Pair<>("web.api.cmd", true), "whether or not the ingame command to manage tokens should be enabled or not");
        config.addKeyValuePair(new Pair<>("web.file.notSupported", "not_supported.html"), "the name of the html file for 'not supported' page");
        config.addKeyValuePair(new Pair<>("logger.verbose", false), "whether or not to log verbose output");
        config.addKeyValuePair(new Pair<>("web.php", false), "enable php");
    }

    private static void assignConfigs() {
        IS_ENABLED = CONFIG.getOrDefault("web.isEnabled", false);
        WEB_PORT = CONFIG.getOrDefault("web.port", 8080);
        WEB_ROOT = CONFIG.getOrDefault("web.root", "webserver/");
        WEB_FILE_ROOT = CONFIG.getOrDefault("web.file.root", "index.html");
        WEB_FILE_404 = CONFIG.getOrDefault("web.file.404", "404.html");
        SERVER_API_ENABLED = CONFIG.getOrDefault("web.api", true);
        ADV_API_ENABLED = CONFIG.getOrDefault("web.api.adv", true);
        API_INGAME_COMMAND_ENABLED = CONFIG.getOrDefault("web.api.cmd", true);
        WEB_FILE_NOSUPPORT = CONFIG.getOrDefault("web.file.notSupported", "not_supported.html");
        VERBOSE = CONFIG.getOrDefault("logger.verbose", true);
        PHP_ENABLED = CONFIG.getOrDefault("web.php", false);
    }
}
