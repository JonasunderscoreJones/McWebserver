package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

import java.util.ArrayList;

@Setter
public class ApiServerMetadataPlayers {
    private int MAX;
    private int ONLINE;
    private ArrayList<ApiServerMetadataPlayer> SAMPLE;
}
