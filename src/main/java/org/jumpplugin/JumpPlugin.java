package org.jumpplugin;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jumpplugin.commands.JumpCommand;
import org.jumpplugin.listeners.CheckpointListener;
import org.jumpplugin.managers.DataManager;
import org.jumpplugin.managers.SessionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class JumpPlugin extends JavaPlugin {

    private DataManager dataManager;
    private SessionManager sessionManager;

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
        sessionManager = new SessionManager();

        this.getCommand("jump").setExecutor(new JumpCommand(this));
        getServer().getPluginManager().registerEvents(new CheckpointListener(this), this);
    }

    public DataManager getDataManager() {
        return dataManager;
    }
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    @Override
    public void onDisable() {
        getLogger().info("Jump Plugin Disabled!");
    }
}
