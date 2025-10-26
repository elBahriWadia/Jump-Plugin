package org.jumpplugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jumpplugin.commands.JumpCommand;
import org.jumpplugin.managers.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JumpPlugin extends JavaPlugin {

    private DataManager dataManager;

    private Map<UUID, String> editingCourse = new HashMap<>();

    public void setEditingCourse(Player player, String courseName) {
        editingCourse.put(player.getUniqueId(), courseName);
    }

    public String getEditingCourse(Player player) {
        return editingCourse.get(player.getUniqueId());
    }

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
