package org.flennn.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.flennn.SpecialItemType;

public class PlayerInteract implements org.bukkit.event.Listener {

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player target)) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        for (SpecialItemType type : SpecialItemType.values()) {
            if (type.isItemMatch(item)) {
                type.onRightClick(player, item, target);
                event.setCancelled(true);
                break;
            }
        }
    }

}
