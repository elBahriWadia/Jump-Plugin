package org.jumpplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jumpplugin.JumpPlugin;

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

        plugin.getDataManager().set("players." + player.getName(), player.getName());
        player.sendMessage("Saved your name to JSON!");


        return true;
    }

}
