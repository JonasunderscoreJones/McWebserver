package me.jonasjones.mcwebserver.util;

import jdk.jfr.StackTrace;
import me.jonasjones.mcwebserver.McWebserver;
import me.jonasjones.mcwebserver.config.ModConfigs;

public class VerboseLogger {
    public static void info(String message) {
        if (ModConfigs.VERBOSE) {
            McWebserver.VERBOSELOGGER.info(message);
        }
    }
    public void debug(String message) {
        if (ModConfigs.VERBOSE) {
            McWebserver.VERBOSELOGGER.debug(message);
        }
    }
    public static void error(String message) {
        if (ModConfigs.VERBOSE) {
            McWebserver.VERBOSELOGGER.error(message);
        }
    }
    public static void trace( String message) {
        if (ModConfigs.VERBOSE) {
            McWebserver.VERBOSELOGGER.trace(message);
        }
    }
    public static void warn( String message) {
        if (ModConfigs.VERBOSE) {
            McWebserver.VERBOSELOGGER.warn(message);
        }
    }
}
