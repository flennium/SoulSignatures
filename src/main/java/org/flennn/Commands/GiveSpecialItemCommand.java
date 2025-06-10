package org.flennn.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.flennn.SpecialItemType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GiveSpecialItemCommand implements CommandExecutor, TabCompleter {

    private final MiniMessage mm = MiniMessage.miniMessage();

    private Component prefix() {
        return mm.deserialize("<gray>[<gradient:#8b00ff:#ff00aa><bold>SoulSignatures</bold></gradient><gray>] <reset>");
    }

    private void send(CommandSender sender, String message) {
        if (sender instanceof Player player) {
            player.sendMessage(prefix().append(mm.deserialize(message)));
        } else {
            sender.sendMessage(mm.stripTags(message));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("soulsignatures.give")) {
            send(sender, "<red>You lack the arcane rights to do this.");
            return true;
        }

        if (args.length == 1) {
            if (!(sender instanceof Player player)) {
                send(sender, "<red>You must specify a player when running this from console.");
                return true;
            }

            String itemName = args[0];
            SpecialItemType type = parseItemType(sender, itemName);
            if (type == null) return true;

            ItemStack item = type.createItem();
            player.getInventory().addItem(item);
            send(player, "<green>The <gold>" + type.name() + "</gold> binds to you...");
            return true;
        }

        if (args.length == 2) {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
                send(sender, "<red>⚠ No soul found by that name: <gray>" + args[0]);
                return true;
            }

            String itemName = args[1];
            SpecialItemType type = parseItemType(sender, itemName);
            if (type == null) return true;

            ItemStack item = type.createItem();
            target.getInventory().addItem(item);

            send(sender, "<green>Bestowed the <gold>" + type.name() + "</gold> upon <yellow>" + target.getName() + "</yellow>.");
            target.sendMessage(prefix().append(mm.deserialize(
                    "<green>You feel a <bold>strange power</bold> awaken within you... <gray>You received: <gold>" + type.name() + "</gold>"
            )));
            return true;
        }

        send(sender, "<red>Usage: </red>/" + label + " <item> OR /" + label + " <player> <item>");
        return true;
    }

    private SpecialItemType parseItemType(CommandSender sender, String input) {
        try {
            return SpecialItemType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            send(sender, "<red>Unknown item type: <gray>" + input);
            send(sender, "<gray>Available: <gold>" +
                    Arrays.stream(SpecialItemType.values())
                            .map(Enum::name)
                            .reduce((a, b) -> a + "</gold>, <gold>" + b)
                            .orElse("None") + "</gold>");
            return null;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                      @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            List<String> suggestions = new ArrayList<>();
            Bukkit.getOnlinePlayers().forEach(p -> suggestions.add(p.getName()));
            Arrays.stream(SpecialItemType.values()).map(Enum::name).forEach(suggestions::add);
            return suggestions.stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            return Arrays.stream(SpecialItemType.values())
                    .map(Enum::name)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return new ArrayList<>();
    }
}
