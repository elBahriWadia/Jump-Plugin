package org.jumpplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jumpplugin.JumpPlugin;

import java.util.HashMap;
import java.util.Map;

public class JumpCommand implements CommandExecutor {

    private final JumpPlugin plugin;

    public JumpCommand(JumpPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String [] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Usage: /jump <create | setstart | setcheckpoint | setend>");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("Usage: /jump create <name>");
                    return true;
                }

                String name = args[1].toLowerCase();

                Object coursesObj = plugin.getDataManager().get("courses");

                if (coursesObj instanceof Map) {
                    Map<String, Object> courses = (Map<String, Object>) coursesObj;

                    if (courses.containsKey(name)) {
                        player.sendMessage("A course named '" + name + "' already exists!");
                        player.sendMessage("Use a different name, or delete it first.");
                        player.sendMessage("For now the course '" + name + "' is set as your active course!");
                        plugin.setEditingCourse(player, name);
                        return true;
                    }
                }

                // Create the course
                plugin.getDataManager().setNested("courses." + name, new HashMap<>());
                plugin.setEditingCourse(player, name);

                player.sendMessage("Course '" + name + "' created successfully and set as your active course!");
                break;

            case "setstart":

                if (args.length > 1) {
                    player.sendMessage("Usage: /jump setstart");
                    return true;
                }

                String course = plugin.getEditingCourse(player);
                if (course == null) {
                    player.sendMessage("You haven't selected or created a course yet. Use /jump create <name> first.");
                    return true;
                }

                Location loc = player.getLocation();
                Map<String, Object> locMap = new HashMap<>();
                locMap.put("world", loc.getWorld().getName());
                locMap.put("x", loc.getX());
                locMap.put("y", loc.getY());
                locMap.put("z", loc.getZ());

                plugin.getDataManager().setNested("courses." + course + ".start", locMap);

                player.sendMessage(String.format(
                        "Start point set for %s at [%.1f, %.1f, %.1f]",
                        course, loc.getX(), loc.getY(), loc.getZ()
                ));
                break;

            case "setcheckpoint":

                if (args.length != 2) {
                    player.sendMessage("Usage: /jump setcheckpoint <id>");
                    return true;
                }

                String checkpointId = args[1];
                if (!checkpointId.matches("\\d+")) {
                    player.sendMessage("The checkpoint ID must be a number!");
                    return true;
                }

                String currentCourse = plugin.getEditingCourse(player);
                if (currentCourse == null) {
                    player.sendMessage("You haven’t selected or created a course yet!");
                    player.sendMessage("Use /jump create <name>§7 first.");
                    return true;
                }

                Location locCp = player.getLocation();
                Map<String, Object> cpLocMap = new HashMap<>();
                cpLocMap.put("world", locCp.getWorld().getName());
                cpLocMap.put("x", locCp.getX());
                cpLocMap.put("y", locCp.getY());
                cpLocMap.put("z", locCp.getZ());

                plugin.getDataManager().setNested("courses." + currentCourse + ".checkpoints." + checkpointId, cpLocMap);

                player.sendMessage(String.format(
                        "Checkpoint %s set for course %s at [%.1f, %.1f, %.1f]",
                        checkpointId, currentCourse, locCp.getX(), locCp.getY(), locCp.getZ()
                ));
                break;

            case "setend":

                if (args.length > 1) {
                    player.sendMessage("Usage: /jump setend");
                    return true;
                }

                String courseEnd = plugin.getEditingCourse(player);
                if (courseEnd == null) {
                    player.sendMessage("You haven’t selected or created a course yet!");
                    player.sendMessage("Use /jump create <name> first.");
                    return true;
                }

                Location locEnd = player.getLocation();
                Map<String, Object> endLocMap = new HashMap<>();
                endLocMap.put("world", locEnd.getWorld().getName());
                endLocMap.put("x", locEnd.getX());
                endLocMap.put("y", locEnd.getY());
                endLocMap.put("z", locEnd.getZ());

                plugin.getDataManager().setNested("courses." + courseEnd + ".end", endLocMap);

                player.sendMessage(String.format(
                        "End point set for %s at [%.1f, %.1f, %.1f]",
                        courseEnd, locEnd.getX(), locEnd.getY(), locEnd.getZ()
                ));
                break;


        }


        return true;
    }

}
