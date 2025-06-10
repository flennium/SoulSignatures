package org.flennn.Handlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface SpecialItemHandler {
    ItemStack createItem();


    boolean isItem(ItemStack item);


    void onRightClick(Player player, ItemStack item, Player target);
}
