package org.jumpplugin.commands;

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
            player.sendMessage("Usage: /jump <create|setstart|setcheckpoint|setend>");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "create":
                if (args.length < 2) {
                    player.sendMessage("§cUsage: /jump create <name>");
                    return true;
                }

                String name = args[1].toLowerCase(); // lowercase for consistency

                // Load current data
                Object coursesObj = plugin.getDataManager().get("courses");

                if (coursesObj instanceof Map) {
                    Map<String, Object> courses = (Map<String, Object>) coursesObj;

                    if (courses.containsKey(name)) {
                        player.sendMessage("§e⚠ A course named '" + name + "' already exists!");
                        player.sendMessage("§7Use a different name, or delete it first.");
                        return true;
                    }
                }

                // Create the course
                plugin.getDataManager().setNested("courses." + name, new HashMap<>());
                plugin.setEditingCourse(player, name);

                player.sendMessage("§a✅ Course '" + name + "' created successfully and set as your active course!");
                break;

        }


        return true;
    }

}
