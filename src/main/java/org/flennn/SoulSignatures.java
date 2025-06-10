package org.flennn;

import org.bukkit.plugin.java.JavaPlugin;
import org.flennn.Commands.GiveSpecialItemCommand;

public final class SoulSignatures extends JavaPlugin {

    private static SoulSignatures instance;

    @Override
    public void onEnable() {
        instance = this;

        GiveSpecialItemCommand command = new GiveSpecialItemCommand();
        this.getCommand("givespecial").setExecutor(command);
        this.getCommand("givespecial").setTabCompleter(command);

        getLogger().info("SoulSignatures has been awakened.");
    }

    @Override
    public void onDisable() {
        getLogger().info("SoulSignatures has fallen into slumber.");
    }

    public static SoulSignatures getInstance() {
        return instance;
    }
}
