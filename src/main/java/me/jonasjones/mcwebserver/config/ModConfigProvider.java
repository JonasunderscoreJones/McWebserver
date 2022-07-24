package me.jonasjones.mcwebserver.config;

import com.mojang.datafixers.util.Pair;

import me.jonasjones.mcwebserver.config.SimpleConfig.DefaultConfig;

import java.util.ArrayList;
import java.util.List;

public class ModConfigProvider implements DefaultConfig {

    private String configContents = "";

    public List<Pair> getConfigsList() {
        return configsList;
    }

    private final List<Pair> configsList = new ArrayList<>();

    public void addKeyValuePair(Pair<String, ?> keyValuePair, String comment) {
        configsList.add(keyValuePair);
        configContents += keyValuePair.getFirst() + "=" + keyValuePair.getSecond() + " #"
                + comment + " | default: " + keyValuePair.getSecond() + "\n";
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}
