package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

import java.util.ArrayList;

@Setter
public class ApiPlayerInfoInventoryEnderChest {
    private ArrayList<ApiPlayerInfoInventoryItemStack> items;

    public ApiPlayerInfoInventoryEnderChest(ArrayList<ApiPlayerInfoInventoryItemStack> items) {
        this.items = items;
    }
}
