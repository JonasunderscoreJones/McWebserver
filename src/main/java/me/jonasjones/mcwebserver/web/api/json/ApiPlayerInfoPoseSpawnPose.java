package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoPoseSpawnPose {
    private ThreeIntegerVector position;
    private float angle;
    private String dimension;

    public ApiPlayerInfoPoseSpawnPose(ThreeIntegerVector position, float angle, String dimension) {
        this.position = position;
        this.angle = angle;
        this.dimension = dimension;
    }
}
