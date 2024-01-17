package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovementEnv {
    private boolean touchingWater;
    private boolean touchingWaterOrRain;
    private boolean submergedInWater;
    private boolean inLava;
    private boolean inPowderSnow;
    private boolean isClimbing;
    private boolean isFallFlying;
    private boolean isSleeping;

    public ApiPlayerInfoMovementEnv(boolean touchingWater, boolean touchingWaterOrRain, boolean submergedInWater, boolean inLava, boolean inPowderSnow, boolean isClimbing, boolean isFallFlying, boolean isSleeping) {
        this.touchingWater = touchingWater;
        this.touchingWaterOrRain = touchingWaterOrRain;
        this.submergedInWater = submergedInWater;
        this.inLava = inLava;
        this.inPowderSnow = inPowderSnow;
        this.isClimbing = isClimbing;
        this.isFallFlying = isFallFlying;
        this.isSleeping = isSleeping;
    }
}
