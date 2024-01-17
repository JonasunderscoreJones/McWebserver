package me.jonasjones.mcwebserver.web.api.json;

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
