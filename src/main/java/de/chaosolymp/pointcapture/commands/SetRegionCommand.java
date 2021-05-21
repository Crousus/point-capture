package de.chaosolymp.pointcapture.commands;

import de.chaosolymp.pointcapture.Region;
import de.chaosolymp.pointcapture.tasks.PlayerRegionTask;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class SetRegionCommand implements CommandExecutor {

    private HashMap<String, PlayerRegionTask> regions;

    public SetRegionCommand(HashMap<String, PlayerRegionTask> regions){
        this.regions = regions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("tower.set")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"Dazu hast du keine Rechte"));
        }

        if(args.length > 2 && regions.containsKey(args[0])){
            Region rg = regions.get(args[0]).getRegions().get(args[1]);
            if(args[2] )
        }

        return false;
    }
}
