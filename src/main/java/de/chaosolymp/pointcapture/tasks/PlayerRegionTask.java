package de.chaosolymp.pointcapture.tasks;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import de.chaosolymp.pointcapture.PointCapture;
import de.chaosolymp.pointcapture.Region;
import de.chaosolymp.pointcapture.config.RegionConfig;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class PlayerRegionTask implements Runnable {

    private Set<String> regionsStrings;
    private FileConfiguration config = RegionConfig.get();
    private HashMap<String, Region> regions = new HashMap<>();
    private String game;

    public PlayerRegionTask(String game){
        ConfigurationSection sub = config.getConfigurationSection(game);
        regionsStrings = sub.getKeys(false);
        this.game = game;
        for(String s : regionsStrings){
            Region rg = new Region();
            rg.setName(sub.getString(s+".region"));
            rg.setMaxCaptureTime(sub.getInt(s+".max-capture-time"));
            rg.setMultiplier(sub.getDouble(s+".player-multiplier"));
            Set<String> locs = sub.getConfigurationSection(s+".tower-locations").getKeys(false);
            Location[] locations = new Location[locs.size()];
            int i = 0;
            for(String current : locs) {
                locations[i] = new Location(Bukkit.getWorld(sub.getString(s+".world")),
                        sub.getDouble(s+".tower-locations."+current+".x"),
                        sub.getDouble(s+".tower-locations."+current+".y"),
                        sub.getDouble(s+".tower-locations."+current+".z"));
                i++;
            }
            rg.setTowers(locations);
            regions.put(s,rg);

            rg.setaCapture(new Location(Bukkit.getWorld(sub.getString(s+".world")),
                    sub.getDouble(s+".capture-teamA-redstoneblock.x"),
                    sub.getDouble(s+".capture-teamA-redstoneblock.y"),
                    sub.getDouble(s+".capture-teamA-redstoneblock.z")));

            rg.setbCapture(new Location(Bukkit.getWorld(sub.getString(s+".world")),
                    sub.getDouble(s+".capture-teamB-redstoneblock.x"),
                    sub.getDouble(s+".capture-teamB-redstoneblock.y"),
                    sub.getDouble(s+".capture-teamB-redstoneblock.z")));

            rg.setNeutralize(new Location(Bukkit.getWorld(sub.getString(s+".world")),
                    sub.getDouble(s+".neutralize-redstoneblock.x"),
                    sub.getDouble(s+".neutralize-redstoneblock.y"),
                    sub.getDouble(s+".neutralize-redstoneblock.z")));
        }
    }

    @Override
    public void run() {
        Collection<Player> players = (Collection<Player>) Bukkit.getOnlinePlayers();

        String teamA = PointCapture.getPlugin().getConfig().getString(game+".teamA");
        String teamB = PointCapture.getPlugin().getConfig().getString(game+".teamB");
        for(Region rg : regions.values()){
            rg.setMembersA(0);
            rg.setMembersB(0);
        }

        for (Player player : players) {
            ApplicableRegionSet set = getRegions(player.getLocation());
            for(ProtectedRegion rg : set.getRegions()){
                System.out.println(rg.getId());
                if(regionsStrings.contains(rg.getId())){
                    if (player.getScoreboard().getTeam(teamA).hasEntry(player.getName())) {
                        regions.get(rg.getId()).addMembersA();
                    } else if (player.getScoreboard().getTeam(teamB).hasEntry(player.getName())) {
                        regions.get(rg.getId()).addMembersB();
                    }
                }
            }
        }
        for(Region rg : regions.values()) {

            if (rg.getScore() >= rg.getMaxCaptureTime()) {
                rg.setLockedScore(rg.getMaxCaptureTime());
                rg.setScore(rg.getMaxCaptureTime());
                rg.getaCapture().getBlock().setType(Material.REDSTONE_BLOCK,true);
                rg.getNeutralize().getBlock().setType(Material.AIR,true);
            } else if (rg.getScore() <= (0 - rg.getMaxCaptureTime())) {
                rg.setLockedScore(0 - rg.getMaxCaptureTime());
                rg.setScore(0-rg.getMaxCaptureTime());
                rg.getbCapture().getBlock().setType(Material.REDSTONE_BLOCK,true);
                rg.getNeutralize().getBlock().setType(Material.AIR,true);
            } else if (rg.getMembersB() == 0 && rg.getMembersA() == 0 && rg.getScore() != 0) {
                if(rg.getLockedScore() > 0) {
                    if (rg.getScore() < rg.getLockedScore()) {
                        rg.setScore(rg.getScore() + 2);
                        if (rg.getScore() > rg.getLockedScore()) {
                            //Autotreturn to captured point by Attackers
                            rg.setScore(rg.getLockedScore());
                            rg.getaCapture().getBlock().setType(Material.REDSTONE_BLOCK,true);
                        }
                    }
                } else if(rg.getLockedScore() < 0){
                    if (rg.getScore() > rg.getLockedScore()) {
                        rg.setScore(rg.getScore() - 2);
                        if (rg.getScore() < rg.getLockedScore()) {
                            //Autotreturn to captured point by Defenders
                            rg.setScore(rg.getLockedScore());
                            rg.getbCapture().getBlock().setType(Material.REDSTONE_BLOCK,true);
                        }

                    }
                } else{
                    if (rg.getScore() > 0) {
                        rg.setScore(rg.getScore() - 5);
                        if (rg.getScore() < 0)
                            rg.setScore(0);
                    } else if (rg.getScore() < 0) {
                        rg.setScore(rg.getScore() + 5);
                        if (rg.getScore() > 0)
                            rg.setScore(0);
                    }
                }
            }

            if (rg.getMembersA() > rg.getMembersB()) {
                if(rg.getLockedScore() <= 0 || rg.getLockedScore() != rg.getScore()) {
                    boolean negative = false;
                    if (rg.getScore() < 0)
                        negative = true;

                    rg.addScore(rg.getMembersA() - rg.getMembersB());
                    if(negative && rg.getScore() >= 0) {
                        rg.setLockedScore(0);
                        rg.getNeutralize().getBlock().setType(Material.REDSTONE_BLOCK,true);
                    }
                    if(rg.getNeutralize().getBlock().getType() != Material.REDSTONE_BLOCK)
                        rg.getbCapture().getBlock().setType(Material.AIR,true);
                }
            } else if (rg.getMembersA() < rg.getMembersB()) {
                if(rg.getLockedScore() >= 0 || rg.getLockedScore() != rg.getScore()){
                    boolean positive = false;
                    if (rg.getScore() > 0)
                        positive = true;
                    rg.decreaseScore(rg.getMembersB() - rg.getMembersA());
                    if(positive && rg.getScore() <= 0) {
                        rg.setLockedScore(0);
                        rg.getNeutralize().getBlock().setType(Material.REDSTONE_BLOCK,true);
                    }

                    if(rg.getNeutralize().getBlock().getType() != Material.REDSTONE_BLOCK)
                        rg.getaCapture().getBlock().setType(Material.AIR,true);
                }
            }
            int i = 0;
            int max = (int) Math.abs(rg.getScore())/(rg.getMaxCaptureTime()/rg.getTowers().length);
            for(Location loc : rg.getTowers()) {
                if (i < max) {
                    if(rg.getScore() < 0)
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.1, 4.0, 0.1, 0.133, new Particle.DustOptions(Color.BLUE, 2f), true);
                    else
                        loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 80, 0.1, 1.0, 0.1, 0.133, new Particle.DustOptions(Color.RED, 2f), true);
                    i++;
                    continue;
                }
                break;
            }
        }

    }

    private static RegionQuery getQuery() {
        return WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
    }

    public static ApplicableRegionSet getRegions(Location loc) {
        return getQuery().getApplicableRegions(BukkitAdapter.adapt(loc));
    }
}
