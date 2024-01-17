package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ThreeDoubleVector {
    private double x;
    private double y;
    private double z;

    public ThreeDoubleVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
