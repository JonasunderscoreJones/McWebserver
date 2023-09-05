package me.jonasjones.mcwebserver.web.api.v1.json;

import com.google.gson.JsonObject;
import lombok.Setter;

@Setter
public class ApiServerMetadata {
    private String DESCRIPTION;
    private JsonObject PLAYERS;
    private JsonObject VERSION;
    private String FAVICON;
    private Boolean SECURE_CHAT_EINFORCED;

}
