package de.chaosolymp.pointcapture.config;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.Scanner;

public class RegionConfig {
    private static File file;
    private static FileConfiguration customFile;
    private final static String FILENAME = "/regions.yml";

    public static boolean setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("PointCapture").getDataFolder(), "regions.yml");
        boolean exist = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                Bukkit.getLogger().info("Creating File");
                copyFile();

            } catch (IOException e) {
                Bukkit.getLogger().info(e.toString());
            }
        } else {
            exist = true;
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        return exist;
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save() {
        try {
            customFile.save(file);
        } catch (IOException e) {
            System.out.println("Couldn't save file");
        }
    }

    private static void copyFile() {
        try {
            InputStream input = RegionConfig.class.getResourceAsStream(FILENAME);
            InputStreamReader reader = new InputStreamReader(input, Charsets.UTF_8);
            Scanner scanner = new Scanner(reader).useDelimiter("\\n");
            FileOutputStream out = new FileOutputStream(file);
            PrintWriter writer = new PrintWriter(out);
            while (scanner.hasNext()) {
                writer.print(scanner.next());
            }
            writer.flush();
            writer.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
