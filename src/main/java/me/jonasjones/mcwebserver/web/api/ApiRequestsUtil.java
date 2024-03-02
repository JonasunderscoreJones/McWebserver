package me.jonasjones.mcwebserver.web.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import lombok.Getter;
import lombok.Setter;
import me.jonasjones.mcwebserver.McWebserver;
import me.jonasjones.mcwebserver.web.api.json.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.entity.boss.CommandBossBar;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static me.jonasjones.mcwebserver.McWebserver.gson;
import static me.jonasjones.mcwebserver.config.ModConfigs.WEB_PORT;
import static me.jonasjones.mcwebserver.web.api.ErrorHandler.internalServerError;

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
    //@Getter @Setter
    //private static float TICK_TIME;
    @Getter @Setter
    private static long TIME_REFERENCE;
    @Getter @Setter
    private static List<ServerPlayerEntity> SERVER_PLAYER_ENTITY_LIST = new ArrayList<>();
    @Getter @Setter
    private static Collection<ResourcePackProfile> SERVER_RESOURCE_PACK_PROFILE_COLLECTION = new ArrayList<>();
    @Getter @Setter
    private static Collection<AdvancementEntry> SERVER_ADVANCEMENT_COLLECTION = new ArrayList<>();
    @Getter @Setter
    private static Collection<CommandBossBar> SERVER_BOSSBAR_COLLECTION = new ArrayList<>();
    private static final ApiServerInfo apiServerInfo = new ApiServerInfo();
    private static final ApiServerMetadata apiServerMetadata = new ApiServerMetadata();
    private static final ApiServerMetadataPlayers apiServerMetadataPlayers = new ApiServerMetadataPlayers();



    public static JsonObject serverMetadata() {
        apiServerMetadataPlayers.setMAX(ApiRequestsUtil.getSERVER_METADATA().players().get().max());
        apiServerMetadataPlayers.setONLINE(ApiRequestsUtil.getSERVER_METADATA().players().get().online());
        apiServerMetadataPlayers.setSAMPLE(convertPlayerList(ApiRequestsUtil.getSERVER_METADATA().players().get().sample()));

        apiServerMetadata.setDESCRIPTION(ApiRequestsUtil.getSERVER_METADATA().description().getString());
        apiServerMetadata.setPLAYERS(JsonParser.parseString(gson.toJson(apiServerMetadataPlayers)).getAsJsonObject());
        apiServerMetadata.setVERSION((JsonObject) JsonParser.parseString("{\"version\":\"" + ApiRequestsUtil.getSERVER_METADATA().version().get().gameVersion() + "\",\"protocol\":" + ApiRequestsUtil.getSERVER_METADATA().version().get().protocolVersion() + "}"));
        if (ApiRequestsUtil.getSERVER_METADATA().favicon().isPresent()) {
            if (!ApiRequestsUtil.getSERVER_IP().isEmpty()) {
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

    public static JsonObject nbtStringToJson(String nbt) {
        if (nbt == null) {
            return null;
        }
        return JsonParser.parseString(nbt).getAsJsonObject();
    }

    public static ArrayList<ApiServerMetadataPlayer> convertPlayerList(List<GameProfile> list) {
        ArrayList<ApiServerMetadataPlayer> players = new ArrayList<>();
        for (GameProfile profile : list) {
            ApiServerMetadataPlayer player = new ApiServerMetadataPlayer();
            player.setID(profile.getId().toString());
            player.setNAME(profile.getName());
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
        apiServerInfo.setTIME_REFERENCE(ApiRequestsUtil.getTIME_REFERENCE());

        return gson.toJsonTree(apiServerInfo).getAsJsonObject();
    }

    public static byte[] getServerIcon() {
        return ApiRequestsUtil.getSERVER_METADATA().favicon().get().iconBytes();
    }

    public static JsonObject playerLookup(String playerName) {
        for (ServerPlayerEntity player : ApiRequestsUtil.getSERVER_PLAYER_ENTITY_LIST()) {
            if (player.getName().getString().equals(playerName)) {
                return JsonParser.parseString("{\"uuid\":\"" + player.getUuidAsString() + "\"}").getAsJsonObject();
            }
        }
        return gson.toJsonTree(ErrorHandler.customError(404, "Player Not Found")).getAsJsonObject();
    }

    public static JsonObject getPlayerInfo(String uuid) {
        for (ServerPlayerEntity player : ApiRequestsUtil.getSERVER_PLAYER_ENTITY_LIST()) {
            if (player.getUuidAsString().equals(uuid)) {
                try {
                    String sleepDirection = (player.getSleepingDirection() != null) ? player.getSleepingDirection().asString() : null;
                    Vec<Integer> sleepPosition = new Vec<>();
                    try {
                        sleepPosition.set(0, player.getSleepingPosition().orElseThrow().getX());
                        sleepPosition.set(1, player.getSleepingPosition().orElseThrow().getY());
                        sleepPosition.set(2, player.getSleepingPosition().orElseThrow().getZ());
                    } catch (Exception ignored) {
                    }
                    Vec<Integer> spawnPointPosition = new Vec<>();
                    try {
                        sleepPosition.set(0, player.getSleepingPosition().orElseThrow().getX());
                        sleepPosition.set(1, player.getSleepingPosition().orElseThrow().getY());
                        sleepPosition.set(2, player.getSleepingPosition().orElseThrow().getZ());
                    } catch (Exception ignored) {}
                    Vec<String> vehicle = new Vec<>();
                    try {
                        vehicle.set(0, Objects.requireNonNull(player.getVehicle()).getName().getString());
                        vehicle.set(1, player.getVehicle().getUuidAsString());
                    } catch (Exception ignored) {}
                    Vec<String> controllingVehicle = new Vec<>();
                    try {
                        controllingVehicle.set(0, Objects.requireNonNull(player.getControllingVehicle()).getName().getString());
                        controllingVehicle.set(1, player.getControllingVehicle().getUuidAsString());
                    } catch (Exception ignored) {}
                    Vec<String> controllingPassenger = new Vec<>();
                    try {
                        controllingPassenger.set(0, Objects.requireNonNull(player.getControllingPassenger()).getName().getString());
                        controllingPassenger.set(1, player.getControllingPassenger().getUuidAsString());
                    } catch (Exception ignored) {}
                    Vec<String> fishHook = new Vec<>();
                    try {
                        fishHook.set(0, Objects.requireNonNull(player.fishHook).getName().getString());
                        fishHook.set(1, player.fishHook.getUuidAsString());
                    } catch (Exception ignored) {}
                    ArrayList<ApiPlayerInfoInventoryItemStack> mainInventory = new ArrayList<>();
                    for (int i = 0; i < player.getInventory().main.size(); i++) {
                        String nbt;
                        try {
                            nbt = Objects.requireNonNull(player.getInventory().main.get(i).getNbt()).toString();
                        } catch (Exception ignored) {
                            nbt = null;
                        }
                        ApiPlayerInfoInventoryItemStack itemStack = new ApiPlayerInfoInventoryItemStack(
                                player.getInventory().main.get(i).getTranslationKey(),
                                player.getInventory().main.get(i).getCount(),
                                nbtStringToJson(nbt)
                        );
                        mainInventory.add(itemStack);
                    }
                    ArrayList<ApiPlayerInfoInventoryItemStack> enderChestInventory = new ArrayList<>();
                    for (int i = 0; i < player.getEnderChestInventory().size(); i++) {
                        String nbt;
                        try {
                            nbt = Objects.requireNonNull(player.getEnderChestInventory().getStack(i).getNbt()).toString();
                        } catch (Exception ignored) {
                            nbt = null;
                        }
                        ApiPlayerInfoInventoryItemStack itemStack = new ApiPlayerInfoInventoryItemStack(
                                player.getEnderChestInventory().getStack(i).getTranslationKey(),
                                player.getEnderChestInventory().getStack(i).getCount(),
                                nbtStringToJson(nbt)
                        );
                        enderChestInventory.add(itemStack);
                    }
                    String armornbt0;
                    try {
                        armornbt0 = Objects.requireNonNull(player.getInventory().armor.get(0).getNbt()).toString();
                    } catch (Exception ignored) {
                        armornbt0 = null;
                    }
                    String armornbt1;
                    try {
                        armornbt1 = Objects.requireNonNull(player.getInventory().armor.get(1).getNbt()).toString();
                    } catch (Exception ignored) {
                        armornbt1 = null;
                    }
                    String armornbt2;
                    try {
                        armornbt2 = Objects.requireNonNull(player.getInventory().armor.get(2).getNbt()).toString();
                    } catch (Exception ignored) {
                        armornbt2 = null;
                    }
                    String armornbt3;
                    try {
                        armornbt3 = Objects.requireNonNull(player.getInventory().armor.get(3).getNbt()).toString();
                    } catch (Exception ignored) {
                        armornbt3 = null;
                    }
                    String offhandnbt;
                    try {
                        offhandnbt = Objects.requireNonNull(player.getInventory().offHand.get(0).getNbt()).toString();
                    } catch (Exception ignored) {
                        offhandnbt = null;
                    }
                    String pickblocktranslationkey;
                    try {
                        pickblocktranslationkey = Objects.requireNonNull(player.getPickBlockStack()).getTranslationKey();
                    } catch (Exception ignored) {
                        pickblocktranslationkey = null;
                    }
                    int pickblockcount;
                    try {
                        pickblockcount = Objects.requireNonNull(player.getPickBlockStack()).getCount();
                    } catch (Exception ignored) {
                        pickblockcount = 0;
                    }
                    String pickblocknbt;
                    try {
                        pickblocknbt = Objects.requireNonNull(player.getPickBlockStack()).toString();
                    } catch (Exception ignored) {
                        pickblocknbt = null;
                    }
                    ApiPlayerInfoInventoryArmor apiPlayerInfoInventoryArmor = new ApiPlayerInfoInventoryArmor(
                            new ApiPlayerInfoInventoryItemStack(
                                    player.getInventory().armor.get(0).getTranslationKey(),
                                    player.getInventory().armor.get(0).getCount(),
                                    nbtStringToJson(armornbt0)
                            ),
                            new ApiPlayerInfoInventoryItemStack(
                                    player.getInventory().armor.get(1).getTranslationKey(),
                                    player.getInventory().armor.get(1).getCount(),
                                    nbtStringToJson(armornbt1)
                            ),
                            new ApiPlayerInfoInventoryItemStack(
                                    player.getInventory().armor.get(2).getTranslationKey(),
                                    player.getInventory().armor.get(2).getCount(),
                                    nbtStringToJson(armornbt2)
                            ),
                            new ApiPlayerInfoInventoryItemStack(
                                    player.getInventory().armor.get(3).getTranslationKey(),
                                    player.getInventory().armor.get(3).getCount(),
                                    nbtStringToJson(armornbt3)
                            )
                    );
                    ApiPlayerInfo apiPlayerInfo = new ApiPlayerInfo();
                    apiPlayerInfo.setProfile(
                            new ApiPlayerInfoProfile(
                                    new ApiPlayerInfoNames(
                                            player.getName().getString(),
                                            player.getGameProfile().getId().toString(),
                                            (player.getCustomName() != null) ? player.getCustomName().getString() : "",
                                            Objects.requireNonNull(player.getDisplayName()).getString(),
                                            player.getStyledDisplayName().getString()
                                    ),
                                    player.age
                            )
                    );
                    ApiPlayerInfoPose pose = new ApiPlayerInfoPose();
                    pose.setBodyYaw(player.bodyYaw);
                    pose.setRandomBodyY(player.getRandomBodyY());
                    pose.setHeadYaw(player.headYaw);
                    pose.setCapePosition(
                            new ThreeDoubleVector(
                                    player.capeX,
                                    player.capeY,
                                    player.capeZ
                            )
                    );
                    pose.setEyePosition(
                            new ThreeDoubleVector(player.getEyePos().x,
                                    player.getEyePos().y,
                                    player.getEyePos().z
                            )
                    );
                    pose.setStandingEyeHeight(player.getStandingEyeHeight());
                    pose.setSwimHeight(player.getSwimHeight());
                    pose.setHeight(player.getHeight());
                    pose.setInvisible(player.isInvisible());
                    pose.setPitch(player.getPitch());
                    pose.setYaw(player.getYaw());
                    pose.setMainArm(player.getMainArm().asString());
                    pose.setX(player.getX());
                    pose.setY(player.getY());
                    pose.setZ(player.getZ());
                    pose.setLimbAnimation(
                            new ApiPlayerInfoPoseLimbAnimation(player.limbAnimator.getPos(),
                                    player.limbAnimator.getSpeed(),
                                    player.limbAnimator.isLimbMoving()
                            )
                    );
                    pose.setRotationClient(
                            new TwoFloatVector(
                                    player.getRotationClient().x,
                                    player.getRotationClient().y
                            )
                    );
                    pose.setRotation(
                            new ThreeDoubleVector(
                                    player.getRotationVector().x,
                                    player.getRotationVector().y,
                                    player.getRotationVector().z
                            )
                    );
                    pose.setBoundingBox(
                            new ApiPlayerInfoPoseBoundingBox(
                                    player.getBoundingBox().minX,
                                    player.getBoundingBox().minY,
                                    player.getBoundingBox().minZ,
                                    player.getBoundingBox().maxX,
                                    player.getBoundingBox().maxY,
                                    player.getBoundingBox().maxZ,
                                    player.getBoundingBox().getAverageSideLength(),
                                    new ThreeDoubleVector(
                                            player.getBoundingBox().getCenter().x,
                                            player.getBoundingBox().getCenter().y,
                                            player.getBoundingBox().getCenter().z
                                    ),
                                    new ThreeDoubleVector(
                                            player.getBoundingBox().getLengthX(),
                                            player.getBoundingBox().getLengthY(),
                                            player.getBoundingBox().getLengthZ()),
                                    player.getBoundingBox().isNaN()
                            )
                    );
                    pose.setChunkPosition(
                            new ApiPlayerInfoPoseChunkPosition(
                                    player.getChunkPos().x,
                                    player.getChunkPos().z,
                                    player.getChunkPos().getStartX(),
                                    player.getChunkPos().getStartZ(),
                                    player.getChunkPos().getCenterX(),
                                    player.getChunkPos().getCenterZ(),
                                    player.getChunkPos().getEndX(),
                                    player.getChunkPos().getEndZ(),
                                    player.getChunkPos().getRegionX(),
                                    player.getChunkPos().getRegionZ()
                            )
                    );
                    pose.setScaleFactor(player.getScaleFactor());
                    pose.setSleepingDirection(sleepDirection);
                    pose.setSleepingPosition(
                            new ThreeIntegerVector(
                                    sleepPosition.get(0),
                                    sleepPosition.get(1),
                                    sleepPosition.get(2)
                            )
                    );
                    pose.setVisibilityBoundingBox(
                            new ApiPlayerInfoPoseVisibilityBoundingBox(
                                    player.getVisibilityBoundingBox().minX,
                                    player.getVisibilityBoundingBox().minY,
                                    player.getVisibilityBoundingBox().minZ,
                                    player.getVisibilityBoundingBox().maxX,
                                    player.getVisibilityBoundingBox().maxY,
                                    player.getVisibilityBoundingBox().maxZ,
                                    player.getVisibilityBoundingBox().getAverageSideLength(),
                                    new ThreeDoubleVector(
                                            player.getVisibilityBoundingBox().getCenter().x,
                                            player.getVisibilityBoundingBox().getCenter().y,
                                            player.getVisibilityBoundingBox().getCenter().z
                                    ),
                                    new ThreeDoubleVector(
                                            player.getVisibilityBoundingBox().getLengthX(),
                                            player.getVisibilityBoundingBox().getLengthY(),
                                            player.getVisibilityBoundingBox().getLengthZ()
                                    ),
                                    player.getVisibilityBoundingBox().isNaN()
                            )
                    );
                    pose.setSpawnPose(
                            new ApiPlayerInfoPoseSpawnPose(
                                    new ThreeIntegerVector(
                                            spawnPointPosition.get(0),
                                            spawnPointPosition.get(1),
                                            spawnPointPosition.get(2)
                                    ),
                                    player.getSpawnAngle(),
                                    player.getSpawnPointDimension().getValue().toUnderscoreSeparatedString()
                            )
                    );
                    apiPlayerInfo.setPose(pose);
                    apiPlayerInfo.setMovement(
                            new ApiPlayerInfoMovement(
                                    player.getMovementDirection().asString(),
                                    new ThreeDoubleVector(
                                            player.getVelocity().x,
                                            player.getVelocity().y,
                                            player.getVelocity().z
                                    ),
                                    new ApiPlayerInfoMovementDistance(
                                            player.distanceTraveled,
                                            player.fallDistance,
                                            player.strideDistance
                                    ),
                                    new ApiPlayerInfoMovementSpeed(
                                            player.speed,
                                            player.forwardSpeed,
                                            player.horizontalSpeed,
                                            player.sidewaysSpeed,
                                            player.upwardSpeed
                                    ),
                                    new ApiPlayerInfoMovementCollision(
                                            player.horizontalCollision,
                                            player.verticalCollision,
                                            player.groundCollision,
                                            player.collidedSoftly,
                                            player.isCollidable()
                                    ),
                                    new ApiPlayerInfoMovementEnv(
                                            player.isTouchingWater(),
                                            player.isTouchingWaterOrRain(),
                                            player.isSubmergedInWater(),
                                            player.isInLava(),
                                            player.inPowderSnow,
                                            player.isClimbing(),
                                            player.isFallFlying(),
                                            player.isSleeping()
                                    ),
                                    new ApiPlayerInfoMovementStepHeight(
                                            player.getStepHeight()
                                    )
                            )
                    );
                    apiPlayerInfo.setVehicleProperties(
                            new ApiPlayerInfoVehicleProperties(
                                    player.hasVehicle(),
                                    player.shouldControlVehicles(),
                                    new ApiPlayerInfoVehiclePropertiesVehicle(
                                            vehicle.get(0),
                                            vehicle.get(1)
                                    ),
                                    new ApiPlayerInfoVehiclePropertiesRootVehicle(
                                            player.getRootVehicle().getName().getString(),
                                            player.getRootVehicle().getUuidAsString()
                                    ),
                                    new ApiPlayerInfoVehiclePropertiesControllingVehicle(
                                            controllingVehicle.get(0),
                                            controllingVehicle.get(1)
                                    ),
                                    new ApiPlayerInfoVehiclePropertiesControllingPassanger(
                                            controllingPassenger.get(0),
                                            controllingPassenger.get(1)
                                    ),
                                    player.canSprintAsVehicle()
                            )
                    );
                    apiPlayerInfo.setFishhook(
                            new ApiPlayerInfoFishhook(
                                fishHook.get(0),
                                fishHook.get(1)
                            )
                    );
                    apiPlayerInfo.setInventory(
                            new ApiPlayerInfoInventory(
                                    new ApiPlayerInfoInventoryMain(
                                            mainInventory
                                    ),
                                    new ApiPlayerInfoInventoryEnderChest(
                                            enderChestInventory
                                    ),
                                    apiPlayerInfoInventoryArmor,
                                    new ApiPlayerInfoInventoryMisc(
                                            new ApiPlayerInfoInventoryItemStack(
                                                    player.getInventory().offHand.get(0).getTranslationKey(),
                                                    player.getInventory().offHand.get(0).getCount(),
                                                    nbtStringToJson(offhandnbt)
                                            ),
                                            new ApiPlayerInfoInventoryItemStack(
                                                    pickblocktranslationkey,
                                                    pickblockcount,
                                                    nbtStringToJson(pickblocknbt)
                                            ),
                                            player.getInventory().selectedSlot
                                    )
                            )
                    );
                    //return gson.toJsonTree(ErrorHandler.customError(404, "Player Not Found")).getAsJsonObject();
                    return gson.toJsonTree(apiPlayerInfo).getAsJsonObject();
                } catch (Exception e) {
                    McWebserver.LOGGER.error("Error while getting player info: " + e);
                    return gson.toJsonTree(internalServerError()).getAsJsonObject();

                }
            }
        }
        return gson.toJsonTree(ErrorHandler.customError(404, "Player Not Found")).getAsJsonObject();
    }
}
