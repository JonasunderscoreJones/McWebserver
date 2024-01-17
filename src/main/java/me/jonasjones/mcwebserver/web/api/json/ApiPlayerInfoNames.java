package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoNames {
    private String name;
    private String playerListName;
    private String customName;
    private String displayName;
    private String styledDisplayName;

    public ApiPlayerInfoNames(String name, String playerListName, String customName, String displayName, String styledDisplayName) {
        this.name = name;
        this.playerListName = playerListName;
        this.customName = customName;
        this.displayName = displayName;
        this.styledDisplayName = styledDisplayName;
    }
}
