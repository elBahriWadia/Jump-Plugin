package org.jumpplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.jumpplugin.commands.JumpCommand;
import org.jumpplugin.managers.DataManager;

public final class JumpPlugin extends JavaPlugin {

    private DataManager dataManager;

    @Override
    public void onEnable() {
        getLogger().info("Jump Plugin Enabled!");

        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        dataManager = new DataManager(getDataFolder());
        this.getCommand("jump").setExecutor(new JumpCommand(this));
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("Jump Plugin Disabled!");
    }
}
