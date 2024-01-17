package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoPoseLimbAnimation {
    private float position;
    private float speed;
    private boolean isMoving;

    public ApiPlayerInfoPoseLimbAnimation(float position, float speed, boolean isMoving) {
        this.position = position;
        this.speed = speed;
        this.isMoving = isMoving;
    }
}
