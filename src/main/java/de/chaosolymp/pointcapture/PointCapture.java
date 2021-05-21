package de.chaosolymp.pointcapture;

import de.chaosolymp.pointcapture.config.RegionConfig;
import de.chaosolymp.pointcapture.tasks.PlayerRegionTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class PointCapture extends JavaPlugin {

    private static PointCapture plugin;
    HashMap<String, PlayerRegionTask> tasks = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        RegionConfig.setup();
        RegionConfig.save();

        this.saveDefaultConfig();

        for(String region: RegionConfig.get().getKeys(false)) {
            PlayerRegionTask task = new PlayerRegionTask(region);
            Bukkit.getScheduler().runTaskTimer(this,task,300l,20l);
            tasks.put(region,task);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PointCapture getPlugin(){
        return plugin;
    }
}
