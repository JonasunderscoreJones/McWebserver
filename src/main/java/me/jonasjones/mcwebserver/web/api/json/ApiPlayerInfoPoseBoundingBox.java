package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoPoseBoundingBox {
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    private double averageSideLength;
    private ThreeDoubleVector center;
    private ThreeDoubleVector size;
    private boolean isEmpty;

    public ApiPlayerInfoPoseBoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, double averageSideLength, ThreeDoubleVector center, ThreeDoubleVector size, boolean isEmpty) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.averageSideLength = averageSideLength;
        this.center = center;
        this.isEmpty = isEmpty;
    }
}
