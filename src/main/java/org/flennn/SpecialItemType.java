package org.flennn;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.flennn.Handlers.BugHunterHandler;
import org.flennn.Handlers.SoulFragmentHandler;
import org.flennn.Handlers.SpecialItemHandler;

public enum SpecialItemType {
    ADMIN_SOUL_FRAGMENT(new SoulFragmentHandler()),
    BUG_HUNTER_FRAGMENT(new BugHunterHandler());

    private final SpecialItemHandler handler;

    SpecialItemType(SpecialItemHandler handler) {
        this.handler = handler;
    }

    public ItemStack createItem() {
        return handler.createItem();
    }

    public boolean isItemMatch(ItemStack item) {
        return handler.isItem(item);
    }

    public void onRightClick(Player player, ItemStack item, Player target) {
        handler.onRightClick(player, item, target);
    }

    public static SpecialItemType fromItem(ItemStack item) {
        for (SpecialItemType type : values()) {
            if (type.handler.isItem(item)) {
                return type;
            }
        }
        return null;
    }

}
