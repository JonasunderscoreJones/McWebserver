package me.jonasjones.mcwebserver.web.api.v1;

import com.google.gson.Gson;

public class ApiRequests {
    private static final Gson gson = new Gson();

    public static String singleValueRequest(String value) {
        return "[\"" + value + "\"]";
    }

    public static String playerNamesRequest() {
        return gson.toJsonTree(ApiRequestsUtil.convertPlayerList(ApiRequestsUtil.getSERVER_METADATA().players().get().sample())).getAsJsonArray().toString();
    }

    public static String serverMetadataRequest() {
        return gson.toJson(ApiRequestsUtil.serverMetadata());
    }

    public static String serverGetAllRequest() {
        return gson.toJson(ApiRequestsUtil.getAll());
    }

    public static String badRequest() {
        return "{\"error\":{\"status\":400,\"message\":\"Bad Request\"}}";
    }

    public static String internalServerError() {
        return "{\"error\":{\"status\":500,\"message\":\"Internal Server Error\"}}";
    }
}
