package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovementDistance {
    private float traveled;
    private float falling;
    private float stride;

    public ApiPlayerInfoMovementDistance(float traveled, float falling, float stride) {
        this.traveled = traveled;
        this.falling = falling;
        this.stride = stride;
    }
}
