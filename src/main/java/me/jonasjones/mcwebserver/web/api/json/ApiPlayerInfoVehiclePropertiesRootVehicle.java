package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoVehiclePropertiesRootVehicle {
    private String name;
    private String uuid;

    public ApiPlayerInfoVehiclePropertiesRootVehicle(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
