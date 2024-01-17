package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoVehiclePropertiesVehicle {
    private String name;
    private String uuid;

    public ApiPlayerInfoVehiclePropertiesVehicle(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
