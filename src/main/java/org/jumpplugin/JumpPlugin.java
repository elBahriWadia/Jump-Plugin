package org.jumpplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jumpplugin.commands.JumpCommand;

public final class JumpPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Jump Plugin Enabled!");

        this.getCommand("jump").setExecutor(new JumpCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Jump Plugin Disabled!");
    }
}
