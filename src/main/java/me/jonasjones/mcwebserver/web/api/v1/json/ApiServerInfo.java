package me.jonasjones.mcwebserver.web.api.v1.json;

import com.google.gson.JsonObject;
import lombok.Setter;

@Setter
public class ApiServerInfo {
    private String SERVER_IP;
    private int SERVER_PORT;
    private String SERVER_NAME;
    private String DEFAULT_GAME_MODE;
    private String LOADER_VERSION;
    private JsonObject METADATA;
    private int TICKS;
    private float TICK_TIME;
    private long TIME_REFERENCE;
}
