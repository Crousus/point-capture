package de.chaosolymp.pointcapture;

import de.chaosolymp.pointcapture.config.RegionConfig;
import de.chaosolymp.pointcapture.tasks.PlayerRegionTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PointCapture extends JavaPlugin {

    private static PointCapture plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        RegionConfig.setup();
        RegionConfig.save();

        this.saveDefaultConfig();

        Bukkit.getScheduler().runTaskTimer(this,new PlayerRegionTask("game1"),300l,20l);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PointCapture getPlugin(){
        return plugin;
    }
}
