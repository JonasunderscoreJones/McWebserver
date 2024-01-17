package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoInventory {
    private ApiPlayerInfoInventoryMain main;
    private ApiPlayerInfoInventoryEnderChest enderChest;
    private ApiPlayerInfoInventoryArmor armor;
    private ApiPlayerInfoInventoryMisc misc;

    public ApiPlayerInfoInventory(ApiPlayerInfoInventoryMain main, ApiPlayerInfoInventoryEnderChest enderChest, ApiPlayerInfoInventoryArmor armor, ApiPlayerInfoInventoryMisc misc) {
        this.main = main;
        this.enderChest = enderChest;
        this.armor = armor;
        this.misc = misc;
    }
}
