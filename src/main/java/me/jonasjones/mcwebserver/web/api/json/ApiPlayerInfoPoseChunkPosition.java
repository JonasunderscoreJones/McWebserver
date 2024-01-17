package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoPoseChunkPosition {
    private int x;
    private int z;
    private int startX;
    private int startZ;
    private int centerX;
    private int centerZ;
    private int endX;
    private int endZ;
    private int regionX;
    private int regionZ;

    public ApiPlayerInfoPoseChunkPosition(int x, int z, int startX, int startZ, int centerX, int centerZ, int endX, int endZ, int regionX, int regionZ) {
        this.x = x;
        this.z = z;
        this.startX = startX;
        this.startZ = startZ;
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.endX = endX;
        this.endZ = endZ;
        this.regionX = regionX;
        this.regionZ = regionZ;
    }
}
