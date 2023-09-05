package me.jonasjones.mcwebserver.web.api.v1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.jonasjones.mcwebserver.web.api.v1.json.ApiServerInfo;
import me.jonasjones.mcwebserver.web.api.v1.json.ApiServerMetadata;
import me.jonasjones.mcwebserver.web.api.v1.json.ApiServerMetadataPlayer;
import me.jonasjones.mcwebserver.web.api.v1.json.ApiServerMetadataPlayers;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_PORT;

public class ApiRequestsUtil {
    @Getter @Setter
    private static String MOTD;
    @Getter @Setter
    private static String SERVER_IP;
    @Getter @Setter
    private static int SERVER_PORT;
    @Getter @Setter
    private static String SERVER_NAME;
    @Getter @Setter
    private static String SERVER_VERSION;
    @Getter @Setter
    private static int CURRENT_PLAYER_COUNT;
    @Getter @Setter
    private static GameMode DEFAULT_GAME_MODE;
    @Getter
    private static final String LOADER_VERSION = FabricLoader.getInstance().getModContainer("fabricloader").get().getMetadata().getVersion().getFriendlyString();
    @Getter @Setter
    private static int MAX_PLAYER_COUNT;
    @Getter @Setter
    private static ServerMetadata SERVER_METADATA;
    @Getter @Setter
    private static int TICKS;
    @Getter @Setter
    private static float TICK_TIME;
    @Getter @Setter
    private static long TIME_REFERENCE;
    @Getter @Setter
    private static List<ServerPlayerEntity> SERVER_PLAYER_ENTITY_LIST = new ArrayList<>();
    @Getter @Setter
    private static Collection<ResourcePackProfile> SERVER_RESOURCE_PACK_PROFILE_COLLECTION = new ArrayList<>();
    @Getter @Setter
    private static Collection<Advancement> SERVER_ADVANCEMENT_COLLECTION = new ArrayList<>();
    @Getter @Setter
    private static Collection<CommandBossBar> SERVER_BOSSBAR_COLLECTION = new ArrayList<>();
    private static final ApiServerInfo apiServerInfo = new ApiServerInfo();
    private static final ApiServerMetadata apiServerMetadata = new ApiServerMetadata();
    private static final ApiServerMetadataPlayers apiServerMetadataPlayers = new ApiServerMetadataPlayers();
    private static final Gson gson = new Gson();


    public static JsonObject serverMetadata() {
        apiServerMetadataPlayers.setMAX(ApiRequestsUtil.getSERVER_METADATA().players().get().max());
        apiServerMetadataPlayers.setONLINE(ApiRequestsUtil.getSERVER_METADATA().players().get().online());
        apiServerMetadataPlayers.setSAMPLE(convertPlayerList(ApiRequestsUtil.getSERVER_METADATA().players().get().sample()));

        apiServerMetadata.setDESCRIPTION(ApiRequestsUtil.getSERVER_METADATA().description().getString());
        apiServerMetadata.setPLAYERS(JsonParser.parseString(gson.toJson(apiServerMetadataPlayers)).getAsJsonObject());
        apiServerMetadata.setVERSION((JsonObject) JsonParser.parseString("{\"version\":\"" + ApiRequestsUtil.getSERVER_METADATA().version().get().gameVersion() + "\",\"protocol\":" + ApiRequestsUtil.getSERVER_METADATA().version().get().protocolVersion() + "}"));
        if (ApiRequestsUtil.getSERVER_METADATA().favicon().isPresent()) {
            if (!ApiRequestsUtil.getSERVER_IP().equals("")) {
                apiServerMetadata.setFAVICON("http://" + ApiRequestsUtil.getSERVER_IP() + ":" + WEB_PORT + "/api/v1/servericon");
            } else {
                apiServerMetadata.setFAVICON("/api/v1/servericon");
            }
        } else {
            apiServerMetadata.setFAVICON(""); // if favicon doesn't exist
        }

        apiServerMetadata.setSECURE_CHAT_EINFORCED(ApiRequestsUtil.getSERVER_METADATA().secureChatEnforced());

        return JsonParser.parseString(gson.toJson(apiServerMetadata)).getAsJsonObject();
    }

    public static ArrayList<ApiServerMetadataPlayer> convertPlayerList(List<GameProfile> list) {
        ArrayList<ApiServerMetadataPlayer> players = new ArrayList<>();
        for (GameProfile profile : list) {
            ApiServerMetadataPlayer player = new ApiServerMetadataPlayer();
            player.setID(profile.getId().toString());
            player.setNAME(profile.getName());
            //player.setPROPERTIES(profile.getProperties().toString()); //Add support for the properties later
            player.setLEGACY(profile.isLegacy());
            players.add(player);
        }
        return players;
    }

    public static JsonObject getAll() {
        apiServerInfo.setSERVER_IP(ApiRequestsUtil.getSERVER_IP());
        apiServerInfo.setSERVER_PORT(ApiRequestsUtil.getSERVER_PORT());
        apiServerInfo.setSERVER_NAME(ApiRequestsUtil.getSERVER_NAME());
        apiServerInfo.setDEFAULT_GAME_MODE(ApiRequestsUtil.getDEFAULT_GAME_MODE().toString());
        apiServerInfo.setLOADER_VERSION(LOADER_VERSION);
        apiServerInfo.setMETADATA(serverMetadata());
        apiServerInfo.setTICKS(ApiRequestsUtil.getTICKS());
        apiServerInfo.setTICK_TIME(ApiRequestsUtil.getTICK_TIME());
        apiServerInfo.setTIME_REFERENCE(ApiRequestsUtil.getTIME_REFERENCE());

        return gson.toJsonTree(apiServerInfo).getAsJsonObject();
    }

    public static byte[] getServerIcon() {
        return ApiRequestsUtil.getSERVER_METADATA().favicon().get().iconBytes();
    }

}
