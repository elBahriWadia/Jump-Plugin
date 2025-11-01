package org.jumpplugin.managers;
import org.bukkit.entity.Player;
import org.jumpplugin.sessions.PlayerSession;

import java.util.*;
import java.util.UUID;

public class SessionManager {
    private final Map<UUID, PlayerSession> sessions = new HashMap<>();

    public void startSession(Player player, String courseName) {
        if (sessions.containsKey(player.getUniqueId())) {
            player.sendMessage("§e⚠ You already have an active session! Use /jump end first.");
            return;
        }
        sessions.put(player.getUniqueId(), new PlayerSession(courseName));
    }

    public PlayerSession getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }


    public void endSession(Player player) {
        sessions.remove(player.getUniqueId());
    }
}
