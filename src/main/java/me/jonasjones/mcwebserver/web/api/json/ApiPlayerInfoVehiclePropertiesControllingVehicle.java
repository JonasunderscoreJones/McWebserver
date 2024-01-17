package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoVehiclePropertiesControllingVehicle {
    private String name;
    private String uuid;

    public ApiPlayerInfoVehiclePropertiesControllingVehicle(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
