package me.jonasjones.mcwebserver.web.api.v1;

import me.jonasjones.mcwebserver.web.api.ApiRequests;
import me.jonasjones.mcwebserver.web.api.ApiRequestsUtil;
import me.jonasjones.mcwebserver.web.api.ErrorHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import static me.jonasjones.mcwebserver.McWebserver.MC_SERVER;

public class ApiV1Handler {

    public static Boolean isApiV1Request(String request) {
        return request.startsWith("/api/v1/");
    }
    public static String handle(String request) {
        switch (request.replace("/api/v1/", "")) {
            // Simple API Requests
            case "motd" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getMOTD());
            }
            case "serverip" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getSERVER_IP());
            }
            case "serverport" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getSERVER_PORT()));
            }
            case "servername" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getSERVER_NAME());
            }
            case "serverversion" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getSERVER_VERSION());
            }
            case "loaderversion" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getLOADER_VERSION());
            }
            case "currentplayercount" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getCURRENT_PLAYER_COUNT()));
            }
            case "defaultgamemode" -> {
                return ApiRequests.singleValueRequest(ApiRequestsUtil.getDEFAULT_GAME_MODE().toString());
            }
            case "maxplayercount" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getMAX_PLAYER_COUNT()));
            }
            case "playernames" -> {
                return ApiRequests.playerNamesRequest();
            }
            case "servermetadata" -> {
                return ApiRequests.serverMetadataRequest();
            }
            case "ticks" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getTICKS()));
            }
            /*case "ticktime" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getTICK_TIME()));
            }*/
            case "timereference" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getTIME_REFERENCE()));
            }
            case "getall" -> {
                return ApiRequests.serverGetAllRequest();
            }
            default -> {
                return ErrorHandler.notFoundErrorString();
            }
        }
    }

    public static void startHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isRunning()) {
                MC_SERVER = server;
                ApiRequestsUtil.setMOTD(server.getServerMotd());
                ApiRequestsUtil.setSERVER_IP(server.getServerIp());
                ApiRequestsUtil.setSERVER_PORT(server.getServerPort());
                ApiRequestsUtil.setSERVER_NAME(server.getName());
                ApiRequestsUtil.setSERVER_VERSION(server.getVersion());
                ApiRequestsUtil.setCURRENT_PLAYER_COUNT(server.getCurrentPlayerCount());
                ApiRequestsUtil.setDEFAULT_GAME_MODE(server.getDefaultGameMode());
                ApiRequestsUtil.setMAX_PLAYER_COUNT(server.getMaxPlayerCount());
                ApiRequestsUtil.setSERVER_METADATA(server.getServerMetadata());
                ApiRequestsUtil.setTICKS(server.getTicks());
                ApiRequestsUtil.setTIME_REFERENCE(server.getTimeReference());
            }
        });
    }
}
