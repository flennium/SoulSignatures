package org.flennn.Handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BugHunterHandler implements SpecialItemHandler{

    private static final NamespacedKey BUG_HUNTER_KEY = new NamespacedKey("SoulSignatures", "BUG_HUNTER_FRAGMENT");
    private static final NamespacedKey BOUND_SOUL_KEY = new NamespacedKey("SoulSignatures", "BOUND_SOUL");
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static final List<Component> alreadyBoundMessages = List.of(
            MM.deserialize("<bold><gradient:#ff0000:#000000>✖ Debugging Conflict!</gradient></bold> <gray>The fragment stutters: <italic>'Breakpoint already set... on another soul.'</italic>"),
            MM.deserialize("<bold><dark_red>☠ Traceback Overflow!</dark_red></bold> <gray>The shard growls: <italic>'Only one stacktrace at a time.'</italic>"),
            MM.deserialize("<gradient:#a00000:#ff0000><bold>⛔ Commit Rejected!</bold></gradient> <gray>This soul has already been pushed to production."),
            MM.deserialize("<bold><red>💢 Double Report Attempt!</red></bold> <gray>The fragment flashes: <italic>'Bug report already logged... with extreme prejudice.'</italic>"),
            MM.deserialize("<gray><italic>The badge sparks violently...</italic></gray> <red><bold>'DEBUG MODE: Soul slot already occupied.'</bold></red>"),
            MM.deserialize("<bold><gradient:#ff0000:#ff5500>✘ Patch Conflict Detected</gradient></bold> <gray><italic>'Merge failed: soul already attached.'</italic>"),
            MM.deserialize("<bold><dark_purple>❌ Already Quarantined!</dark_purple></bold> <gray>The fragment snarls: <italic>'One infected soul at a time.'</italic>"),
            MM.deserialize("<gradient:#800000:#4d0000><bold>⚠ Internal Error:</bold></gradient> <gray>The badge logs: <italic>'SoulPointerException: target already bound.'</italic>"),
            MM.deserialize("<bold><red>✖ Hotfix Denied</red></bold> <gray>The fragment blinks: <italic>'Only one bug bounty per soul.'</italic>"),
            MM.deserialize("<bold><gradient:#ff5f5f:#990000>💀 The badge glows red-hot</gradient></bold> <gray><italic>'Another hunter's soul is already sealed within.'</italic>")
    );


    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName("§5🔧 §5Bug Hunter Fragment");

            List<String> lore = new ArrayList<>();
            lore.add("§8• §7Patch notes fear this item 📢");
            lore.add("§8• §7Right-click to bind to a player");

            meta.setLore(lore);
            meta.getPersistentDataContainer().set(BOUND_SOUL_KEY, PersistentDataType.STRING, "");
            item.setItemMeta(meta);
        }

        return item;
    }


    public boolean isItem(ItemStack item) {
        if (item == null || item.getType() != Material.NETHER_STAR || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(BOUND_SOUL_KEY, PersistentDataType.STRING);
    }


    public void onRightClick(Player player, ItemStack item, Player target) {
        if (!isItem(item)) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        String currentBound = container.get(BUG_HUNTER_KEY, PersistentDataType.STRING);

        if (currentBound != null && !currentBound.isEmpty()) {
            Component rejection = alreadyBoundMessages.get(new Random().nextInt(alreadyBoundMessages.size()));
            player.sendMessage(rejection);
            return;
        }

        String targetName = target.getName();
        container.set(BUG_HUNTER_KEY, PersistentDataType.STRING, target.getUniqueId().toString());

        meta.setDisplayName("§5🔧 " + targetName + "'s §5BugHunter Badge");

        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();

        if (lore.size() >= 2) {
            lore.set(1, "§8• §7Bound Soul: §c" + targetName);
        } else {
            lore.add("§8• §7Bound Soul: §c" + targetName);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        Component success = MM.deserialize(
                "<green><bold>✔ Soul Bound!</bold></green> <gray>The fragment accepts <red><target></red> as its eternal host.",
                Placeholder.unparsed("target", targetName)
        );
        player.sendMessage(success);
    }
}
