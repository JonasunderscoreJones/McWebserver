package me.jonasjones.mcwebserver.web.api;

import me.jonasjones.mcwebserver.web.api.json.Error;

import static me.jonasjones.mcwebserver.McWebserver.gson;

public class ErrorHandler {
    public static Error badRequest() {
        return new Error(400, "Bad Request");
    }

    public static String badRequestString() {
        return gson.toJsonTree(badRequest()).getAsJsonObject().toString();
    }

    public static Error unauthorized() {
        return new Error(401, "Unauthorized");
    }

    public static String unauthorizedString() {
        return gson.toJsonTree(unauthorized()).getAsJsonObject().toString();
    }

    public static Error internalServerError() {
        return new Error(500, "Internal Server Error");
    }

    public static String internalServerErrorString() {
        return gson.toJsonTree(internalServerError()).getAsJsonObject().toString();
    }

    public static Error forbiddenRequest() {
        return new Error(403, "Forbidden");
    }

    public static String forbiddenRequestString() {
        return gson.toJsonTree(forbiddenRequest()).getAsJsonObject().toString();
    }

    public static Error notFoundError() {
        return new Error(404, "Not Found");
    }

    public static String notFoundErrorString() {
        return gson.toJsonTree(notFoundError()).getAsJsonObject().toString();
    }

    public static Error notImplementedError() {
        return new Error(501, "Not Implemented");
    }

    public static String notImplementedErrorString() {
        return gson.toJsonTree(notImplementedError()).getAsJsonObject().toString();
    }

    public static Error customError(int status, String message) {
        return new Error(status, message);
    }

    public static String customErrorString(int status, String message) {
        return gson.toJsonTree(customError(status, message)).getAsJsonObject().toString();
    }
}
