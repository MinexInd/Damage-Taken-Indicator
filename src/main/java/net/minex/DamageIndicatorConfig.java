package net.minex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DamageIndicatorConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("damage-taken-indicator.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static long lastModified = -1;
    
    public static ConfigData instance = new ConfigData();

    private static final Map<String, Integer> COLOR_MAP = new HashMap<>();
    static {
        COLOR_MAP.put("RED", 0xFF0000);
        COLOR_MAP.put("GREEN", 0x00FF00);
        COLOR_MAP.put("GOLD", 0xFFD700);
        COLOR_MAP.put("BLUE", 0x0000FF);
        COLOR_MAP.put("YELLOW", 0xFFFF00);
        COLOR_MAP.put("WHITE", 0xFFFFFF);
        COLOR_MAP.put("AQUA", 0x00FFFF);
        COLOR_MAP.put("PURPLE", 0x800080);
    }

    public static class ConfigData {
        public String damageColor = "RED";
        public String healingColor = "GREEN";
        public String criticalColor = "GOLD";
        public float scale = 1.0f;
        public int xOffset = 0;
        public int yOffset = 0;
        public float criticalThreshold = 3.0f;
        public boolean enableAccumulation = true;
        public int accumulationResetTicks = 40;
    }

    public static int getColor(String colorName) {
        return COLOR_MAP.getOrDefault(colorName.toUpperCase(), 0xFFFFFF);
    }

    public static void checkReload() {
        if (CONFIG_FILE.exists() && CONFIG_FILE.lastModified() > lastModified) {
            load();
            lastModified = CONFIG_FILE.lastModified();
        }
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, ConfigData.class);
                lastModified = CONFIG_FILE.lastModified();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
