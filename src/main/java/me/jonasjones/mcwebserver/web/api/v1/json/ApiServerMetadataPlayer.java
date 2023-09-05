package me.jonasjones.mcwebserver.web.api.v1.json;

import lombok.Setter;

@Setter
public class ApiServerMetadataPlayer {
    private String ID;
    private String NAME;
    private String PROPERTIES;
    private Boolean LEGACY;
}
