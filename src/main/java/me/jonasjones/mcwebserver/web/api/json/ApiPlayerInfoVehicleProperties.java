package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoVehicleProperties {
    private boolean hasVehicle;
    private boolean shouldControlVehicles;
    private ApiPlayerInfoVehiclePropertiesVehicle vehicle;
    private ApiPlayerInfoVehiclePropertiesRootVehicle rootVehicle;
    private ApiPlayerInfoVehiclePropertiesControllingPassanger controllingPassanger;
    private ApiPlayerInfoVehiclePropertiesControllingVehicle controllingVehicle;
    private boolean canSprintAsVehicle;

    public ApiPlayerInfoVehicleProperties(boolean hasVehicle, boolean shouldControlVehicles, ApiPlayerInfoVehiclePropertiesVehicle vehicle, ApiPlayerInfoVehiclePropertiesRootVehicle rootVehicle, ApiPlayerInfoVehiclePropertiesControllingVehicle controllingVehicle, ApiPlayerInfoVehiclePropertiesControllingPassanger controllingPassanger, boolean canSprintAsVehicle) {
        this.hasVehicle = hasVehicle;
        this.shouldControlVehicles = shouldControlVehicles;
        this.vehicle = vehicle;
        this.rootVehicle = rootVehicle;
        this.controllingPassanger = controllingPassanger;
        this.controllingVehicle = controllingVehicle;
        this.canSprintAsVehicle = canSprintAsVehicle;
    }
}
