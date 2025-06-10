package org.flennn.Handlers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SoulFragmentHandler implements SpecialItemHandler {

    private static final NamespacedKey SOUL_FRAGMENT_KEY = new NamespacedKey("SoulSignatures", "SOUL_FRAGMENT");
    private static final NamespacedKey BOUND_SOUL_KEY = new NamespacedKey("SoulSignatures", "BOUND_SOUL");
    private static final MiniMessage MM = MiniMessage.miniMessage();

    private static final List<Component> alreadyBoundMessages = List.of(
            MM.deserialize("<gradient:#8b0000:#ff0000><bold>⚠ Soul Overload!</bold></gradient> <gray>One fragment. One soul. Yours is <obfuscated>not</obfuscated> the chosen one."),
            MM.deserialize("<bold><dark_purple>⛔ Forbidden Ritual!</dark_purple></bold> <gray>The fragment growls... <italic>'I already feast on another.'</italic>"),
            MM.deserialize("<bold><red>⚠ Rejected!</red></bold> <gray>The shard twitches. <italic>It already sings the song of another's screams.</italic>"),
            MM.deserialize("<gradient:#ff5f5f:#ff0000><bold>💢 Denied!</bold></gradient> <gray>The badge whispers: <italic>'One soul at a time, greedy mortal.'</italic>"),
            MM.deserialize("<dark_gray><italic>The fragment shivers violently...</italic> <red><bold>'There’s already a soul in here, champ.'</bold></red>"),
            MM.deserialize("<bold><gradient:#ff0000:#000000>Error:</gradient></bold> <gray>Soul slot already filled. Please uninstall previous entity."),
            MM.deserialize("<bold><dark_red>The soul fragment recoils!</dark_red></bold> <gray><italic>'Another soul? I’m full, thanks.'</italic>"),
            MM.deserialize("<bold><gradient:#222222:#770000>The void laughs:</gradient></bold> <italic><dark_red>'Double bind attempt? Cute.'</dark_red></italic>"),
            MM.deserialize("<red><bold>✘ Ritual Failed</bold></red> <gray>The fragment resists. You hear... laughter. Lots of it."),
            MM.deserialize("<bold><gold>⚠ System Alert:</gold></bold> <gray>Duplicate soul detected. The badge flashes <gradient:#ff0000:#000000>RED</gradient>."),
            MM.deserialize("<dark_purple><italic>The fragment howls in rage:</italic></dark_purple> <red><bold>'BACK OFF, THIS ONE’S MINE.'</bold></red>"),
            MM.deserialize("<bold><gray>The badge begins to steam...</gray> <gradient:#ff0000:#aa0000>'Do not tempt fate again.'</gradient></bold>"),
            MM.deserialize("<bold><gradient:#550000:#220000><italic>'You ever try stuffing 2 ghosts in one jar?'</italic></gradient></bold> <gray>Bad idea. Very bad."),
            MM.deserialize("<dark_gray><italic>The fragment pulses with anger...</italic></dark_gray> <red><bold>'This soul’s already in the jar, nerd.'</bold></red>"),
            MM.deserialize("<bold><red>🔥 Soul Conflict!</red></bold> <gray>Your attempt causes <obfuscated>weird</obfuscated> vibrations. Stop it."),
            MM.deserialize("<gradient:#000000:#cc0000><bold><italic>'One soul. One fate.'</italic></bold></gradient> <gray>The badge defies you."),
            MM.deserialize("<bold><dark_red>[!] ERROR:</dark_red></bold> <italic>Multiple souls detected.</italic> <gray>Restart the ritual and try again."),
            MM.deserialize("<bold><yellow>💡 Tip:</yellow></bold> <gray>Trying to double-bind a soul? That’s how you get cursed."),
            MM.deserialize("<bold><dark_purple>The fragment speaks:</dark_purple></bold> <italic><red>'You are... too late.'</red></italic>"),
            MM.deserialize("<gradient:#660000:#330000><bold><italic>'The gate is closed.'</italic></bold></gradient> <gray>Binding aborted.")
    );


    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        meta.displayName(MM.deserialize("<dark_purple>☠ Soul Fragment"));
        meta.lore(List.of(
                MM.deserialize("<gray>• <white>An arcane relic capable of binding a single soul."),
                MM.deserialize("<gray>• <white>Right-click a player to absorb their essence.")
        ));

        meta.getPersistentDataContainer().set(SOUL_FRAGMENT_KEY, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);
        return item;
    }


    @Override
    public boolean isItem(ItemStack item) {
        if (item == null || item.getType() != Material.ENDER_EYE) return false;
        if (!item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(SOUL_FRAGMENT_KEY, PersistentDataType.BYTE);
    }

    @Override
    public void onRightClick(Player player, ItemStack item, Player target) {
        if (!isItem(item)) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        if (meta.getPersistentDataContainer().has(BOUND_SOUL_KEY, PersistentDataType.STRING)) {
            Component message = alreadyBoundMessages.get((int) (Math.random() * alreadyBoundMessages.size()));
            player.sendMessage(message);
            return;
        }

        Component targetName = target.displayName();

        meta.displayName(MM.deserialize("<dark_purple>☠ " + targetName + "'s Soul Fragment"));

        meta.getPersistentDataContainer().set(BOUND_SOUL_KEY, PersistentDataType.STRING, target.getUniqueId().toString());

        List<Component> lore = new ArrayList<>();
        lore.add(MM.deserialize("<gray>• <white>An arcane relic capable of binding a single soul."));
        lore.add(MM.deserialize("<gray>• <white>Bound Soul: <red>" + targetName));
        meta.lore(lore);

        item.setItemMeta(meta);

        player.sendMessage(MM.deserialize("<green>You have bound the soul of <yellow>" + targetName + "</yellow> <green>to the fragment."));
    }



}
