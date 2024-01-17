package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoPose {
    private float bodyYaw;
    private double randomBodyY;
    private float headYaw;
    private ThreeDoubleVector capePosition;
    private ThreeDoubleVector eyePosition;
    private float standingEyeHeight;
    private double swimHeight;
    private float height;
    private boolean isInvisible;
    private float pitch;
    private float yaw;
    private String mainArm;
    private double x;
    private double y;
    private double z;
    private ApiPlayerInfoPoseLimbAnimation limbAnimation;
    private TwoFloatVector rotationClient;
    private ThreeDoubleVector rotation;
    private ApiPlayerInfoPoseBoundingBox boundingBox;
    private ApiPlayerInfoPoseChunkPosition chunkPosition;
    private float scaleFactor;
    private String sleepingDirection;
    private ThreeIntegerVector sleepingPosition;
    private ApiPlayerInfoPoseVisibilityBoundingBox visibilityBoundingBox;
    private ApiPlayerInfoPoseSpawnPose spawnPose;
}
