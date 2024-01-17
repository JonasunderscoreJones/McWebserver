package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovementSpeed {
    private float speed;
    private float forward;
    private float horizontal;
    private float sideways;
    private float upward;

    public ApiPlayerInfoMovementSpeed(float speed, float forward, float horizontal, float sideways, float upward) {
        this.speed = speed;
        this.forward = forward;
        this.horizontal = horizontal;
        this.sideways = sideways;
        this.upward = upward;
    }
}
