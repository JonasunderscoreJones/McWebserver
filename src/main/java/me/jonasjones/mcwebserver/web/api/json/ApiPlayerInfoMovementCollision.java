package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoMovementCollision {
    private boolean horizontal;
    private boolean vertical;
    private boolean ground;
    private boolean softly;
    private boolean iscollidable;

    public ApiPlayerInfoMovementCollision(boolean horizontal, boolean vertical, boolean ground, boolean softly, boolean iscollidable) {
        this.horizontal = horizontal;
        this.vertical = vertical;
        this.ground = ground;
        this.softly = softly;
        this.iscollidable = iscollidable;
    }
}
