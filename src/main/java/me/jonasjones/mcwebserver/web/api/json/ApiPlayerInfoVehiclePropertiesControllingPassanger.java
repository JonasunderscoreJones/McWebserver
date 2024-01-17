package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoVehiclePropertiesControllingPassanger {
    private String name;
    private String uuid;

    public ApiPlayerInfoVehiclePropertiesControllingPassanger(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
