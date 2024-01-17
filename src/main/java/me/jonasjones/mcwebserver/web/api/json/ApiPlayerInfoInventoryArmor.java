package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoInventoryArmor {
    private ApiPlayerInfoInventoryItemStack head;
    private ApiPlayerInfoInventoryItemStack chest;
    private ApiPlayerInfoInventoryItemStack legs;
    private ApiPlayerInfoInventoryItemStack feet;

    public ApiPlayerInfoInventoryArmor(ApiPlayerInfoInventoryItemStack head, ApiPlayerInfoInventoryItemStack chest, ApiPlayerInfoInventoryItemStack legs, ApiPlayerInfoInventoryItemStack feet) {
        this.head = head;
        this.chest = chest;
        this.legs = legs;
        this.feet = feet;
    }
}
