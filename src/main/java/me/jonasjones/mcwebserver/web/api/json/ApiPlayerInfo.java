package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfo {
    private ApiPlayerInfoProfile profile;
    private ApiPlayerInfoPose pose;
    private ApiPlayerInfoMovement movement;
    private ApiPlayerInfoVehicleProperties vehicleProperties;
    private ApiPlayerInfoFishhook fishhook;
    private ApiPlayerInfoInventory inventory;
}
