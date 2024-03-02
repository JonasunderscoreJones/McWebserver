package me.jonasjones.mcwebserver.web.api.v2;

import me.jonasjones.mcwebserver.McWebserver;
import me.jonasjones.mcwebserver.web.api.ApiRequests;
import me.jonasjones.mcwebserver.web.api.ApiRequestsUtil;
import me.jonasjones.mcwebserver.web.api.ErrorHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import java.security.NoSuchAlgorithmException;

import static me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenManager.isTokenValid;

public class ApiV2Handler {
    public static Boolean isApiV2Request(String request) {
        return request.startsWith("/api/v2/");
    }

    public static String handle(String request, String token) throws NoSuchAlgorithmException {
        if (token == null) {
            return ErrorHandler.forbiddenRequestString();
        }

        boolean isTokenValid = isTokenValid(token);

        switch (request.replace("/api/v2/", "")) {
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
            case "timereference" -> {
                return ApiRequests.singleValueRequest(String.valueOf(ApiRequestsUtil.getTIME_REFERENCE()));
            }
            case "getall" -> {
                return ApiRequests.serverGetAllRequest();
            }
        }

        if (isTokenValid) {
            request = request.replace("/api/v2/", "");
            if (request.startsWith("playerlookup?uuid=")) {
                return ApiRequests.playerLookupRequest(request.replace("playerlookup?uuid=", ""));
            } else if (request.startsWith("playerinfo/inventory?uuid=")) {
                String playerName = request.replace("playerinfo/inventory?uuid=", "");
                //return ApiRequests.playerInfoRequest(playerName);
                return ErrorHandler.internalServerErrorString();
            } else if (request.startsWith("playerinfo/inventory?playeruuid=")) {
                //return ApiRequests.playerInfoRequestFromUuid(request.replace("playerinfo/inventory?playeruuid=", ""));
                return ErrorHandler.internalServerErrorString();
            } else if (request.startsWith("playerinfo/enderchest?playername=")) {
                String playerName = request.replace("playerinfo/enderchest?playername=", "");
                //return ApiRequests.playerInfoRequest(playerName);
                return ErrorHandler.internalServerErrorString();
            } else if (request.startsWith("playerinfo/enderchest?playeruuid=")) {
                //return ApiRequests.playerInfoRequestFromUuid(request.replace("playerinfo/enderchest?playeruuid=", ""));
                return ErrorHandler.internalServerErrorString();
            } else {
                return ErrorHandler.notFoundErrorString();
            }
        } else {
            return ErrorHandler.forbiddenRequestString();
        }
    }

    public static void startAdvHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (server.isRunning()) {
                ApiRequestsUtil.setSERVER_PLAYER_ENTITY_LIST(server.getPlayerManager().getPlayerList());
                ApiRequestsUtil.setSERVER_RESOURCE_PACK_PROFILE_COLLECTION(server.getDataPackManager().getProfiles());
                ApiRequestsUtil.setSERVER_ADVANCEMENT_COLLECTION(server.getAdvancementLoader().getAdvancements());
                ApiRequestsUtil.setSERVER_BOSSBAR_COLLECTION(server.getBossBarManager().getAll());
                ApiRequestsUtil.getSERVER_PLAYER_ENTITY_LIST().forEach(serverPlayerEntity -> {

                });
            }
        });
    }
}
