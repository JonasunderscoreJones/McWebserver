package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoFishhook {
    private String name;
    private String uuid;

    public ApiPlayerInfoFishhook(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
