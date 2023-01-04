package net.chargonium.backdoordetector;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class shutdownPluginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("Backdoor-Detector"));
        return true;
    }
}
