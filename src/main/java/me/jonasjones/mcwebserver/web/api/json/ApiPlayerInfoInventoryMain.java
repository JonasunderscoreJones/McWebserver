package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

import java.util.ArrayList;

@Setter
public class ApiPlayerInfoInventoryMain {
    private ArrayList<ApiPlayerInfoInventoryItemStack> items;

    public ApiPlayerInfoInventoryMain(ArrayList<ApiPlayerInfoInventoryItemStack> items) {
        this.items = items;
    }
}
