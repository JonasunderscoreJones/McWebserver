package me.jonasjones.mcwebserver.web.api.json;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import lombok.Setter;

@Setter
public class ApiPlayerInfoInventoryItemStack {
    private String translationKey;
    private Integer count;
    private JsonObject nbt;

    public ApiPlayerInfoInventoryItemStack(String translationKey, Integer count, JsonObject nbt) {
        this.translationKey = translationKey;
        this.count = count;
        this.nbt = nbt;
    }
}
