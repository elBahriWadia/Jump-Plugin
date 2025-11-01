package org.jumpplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jumpplugin.JumpPlugin;
import org.jumpplugin.managers.DataManager;
import org.jumpplugin.managers.SessionManager;
import org.jumpplugin.sessions.PlayerSession;

import java.util.Map;

public class CheckpointListener implements Listener {

    private final JumpPlugin plugin;

    public CheckpointListener(JumpPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Get the player's session (only if they are in a jump run)
        SessionManager sessionManager = plugin.getSessionManager();
        PlayerSession session = sessionManager.getSession(player);
        if (session == null) return; // player not in active run

        String courseName = session.getCourseName();
        DataManager data = plugin.getDataManager();

        // --- Checkpoint detection ---
        Map<String, Object> checkpointsMap = (Map<String, Object>)
                data.getNested("courses." + courseName + ".checkpoints");
        if (checkpointsMap == null) return;

        for (Map.Entry<String, Object> entry : checkpointsMap.entrySet()) {
            int id = Integer.parseInt(entry.getKey());
            Map<String, Object> locData = (Map<String, Object>) entry.getValue();

            Location checkpointLoc = new Location(
                    Bukkit.getWorld((String) locData.get("world")),
                    ((Number) locData.get("x")).doubleValue(),
                    ((Number) locData.get("y")).doubleValue(),
                    ((Number) locData.get("z")).doubleValue()
            );

            if (player.getLocation().distance(checkpointLoc) < 1.5) {
                if (id > session.getLastCheckpointId()) {
                    session.setLastCheckpointId(id);
                    session.addPoint();
                    player.sendMessage("§aCheckpoint " + id + " reached! (+1 point)");
                }
            }
        }

        // --- Fall detection (example Y threshold = 40) ---
        double fallY = 40.0;
        if (player.getLocation().getY() < fallY) {
            int lastId = session.getLastCheckpointId();
            Location tpLoc;

            if (lastId == 0) {
                Map<String, Object> start = (Map<String, Object>)
                        data.getNested("courses." + courseName + ".start");
                tpLoc = new Location(
                        Bukkit.getWorld((String) start.get("world")),
                        ((Number) start.get("x")).doubleValue(),
                        ((Number) start.get("y")).doubleValue(),
                        ((Number) start.get("z")).doubleValue()
                );
            } else {
                Map<String, Object> cp = (Map<String, Object>)
                        data.getNested("courses." + courseName + ".checkpoints." + lastId);
                tpLoc = new Location(
                        Bukkit.getWorld((String) cp.get("world")),
                        ((Number) cp.get("x")).doubleValue(),
                        ((Number) cp.get("y")).doubleValue(),
                        ((Number) cp.get("z")).doubleValue()
                );
            }

            player.teleport(tpLoc);
            player.sendMessage("§cYou fell! Returned to your last checkpoint.");
        }
    }
}
