package org.jumpplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jumpplugin.JumpPlugin;

import java.util.HashMap;

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
                    player.sendMessage("Usage: /jump create <name>");
                    return true;
                }
                String name = args[1];
                plugin.getDataManager().set("courses." + name, new HashMap<>());
                player.sendMessage("Course '" + name + "' created!");
                break;

            default:
                player.sendMessage("Unknown subcommand.");
                break;
        }


        return true;
    }

}
