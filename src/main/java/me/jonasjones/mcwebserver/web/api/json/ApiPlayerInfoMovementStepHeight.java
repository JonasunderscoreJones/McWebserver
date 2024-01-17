package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovementStepHeight {
    private float stepHeight;

    public ApiPlayerInfoMovementStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
    }
}
