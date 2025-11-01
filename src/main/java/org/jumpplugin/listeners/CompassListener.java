package org.jumpplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jumpplugin.JumpPlugin;
import org.jumpplugin.sessions.PlayerSession;

import java.util.Map;

public class CompassListener implements Listener {

    private final JumpPlugin plugin;

    public CompassListener(JumpPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.COMPASS) return;
        if (!item.hasItemMeta() || !item.getItemMeta().getDisplayName().contains("Return")) return;

        PlayerSession session = plugin.getSessionManager().getSession(player);
        if (session == null) {
            player.sendMessage("§cYou are not currently in a jump session.");
            return;
        }

        int lastId = session.getLastCheckpointId();
        String courseName = session.getCourseName();

        // Get checkpoint or start if none
        Map<String, Object> cp;
        if (lastId == 0) {
            cp = (Map<String, Object>) plugin.getDataManager()
                    .getNested("courses." + courseName + ".start");
        } else {
            cp = (Map<String, Object>) plugin.getDataManager()
                    .getNested("courses." + courseName + ".checkpoints." + lastId);
        }

        if (cp == null) {
            player.sendMessage("§cNo checkpoint found to return to.");
            return;
        }

        Location cpLoc = new Location(
                Bukkit.getWorld((String) cp.get("world")),
                ((Number) cp.get("x")).doubleValue(),
                ((Number) cp.get("y")).doubleValue(),
                ((Number) cp.get("z")).doubleValue()
        );

        player.teleport(cpLoc);
        player.sendMessage("§bReturned to your last checkpoint!");
    }
}
