package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovement {
    private String direction;
    private ThreeDoubleVector velocity;
    private ApiPlayerInfoMovementDistance distance;
    private ApiPlayerInfoMovementSpeed speed;
    private ApiPlayerInfoMovementCollision collision;
    private ApiPlayerInfoMovementEnv env;
    private ApiPlayerInfoMovementStepHeight stepHeight;

    public ApiPlayerInfoMovement(String direction, ThreeDoubleVector velocity, ApiPlayerInfoMovementDistance distance, ApiPlayerInfoMovementSpeed speed, ApiPlayerInfoMovementCollision collision, ApiPlayerInfoMovementEnv env, ApiPlayerInfoMovementStepHeight stepHeight) {
        this.direction = direction;
        this.velocity = velocity;
        this.distance = distance;
        this.speed = speed;
        this.collision = collision;
        this.env = env;
        this.stepHeight = stepHeight;
    }
}
