package me.jonasjones.mcwebserver.web.api.json;

import lombok.Setter;

@Setter
public class ApiPlayerInfoInventoryMisc {
    private ApiPlayerInfoInventoryItemStack offHand;
    private ApiPlayerInfoInventoryItemStack pickBlock;
    private int selectedSlot;

    public ApiPlayerInfoInventoryMisc(ApiPlayerInfoInventoryItemStack offHand, ApiPlayerInfoInventoryItemStack pickBlock, int selectedSlot) {
        this.offHand = offHand;
        this.pickBlock = pickBlock;
    }
}
