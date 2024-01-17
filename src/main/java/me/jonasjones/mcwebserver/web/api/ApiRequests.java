package me.jonasjones.mcwebserver.web.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.jonasjones.mcwebserver.McWebserver;

public class ApiRequests {
    private static final Gson gson = new Gson();

    public static String singleValueRequest(String value) {
        return "[\"" + value + "\"]";
    }

    public static String playerNamesRequest() {
        return gson.toJsonTree(ApiRequestsUtil.convertPlayerList(ApiRequestsUtil.getSERVER_METADATA().players().get().sample())).getAsJsonArray().toString();
    }

    public static String playerInfoRequest(String playerName) {
        return gson.toJson(ApiRequestsUtil.getPlayerInfo(playerName));
    }

    public static String serverMetadataRequest() {
        return gson.toJson(ApiRequestsUtil.serverMetadata());
    }

    public static String serverGetAllRequest() {
        return gson.toJson(ApiRequestsUtil.getAll());
    }
}
