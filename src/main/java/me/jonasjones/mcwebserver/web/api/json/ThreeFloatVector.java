package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ThreeFloatVector {
    private float x;
    private float y;
    private float z;

    public ThreeFloatVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
