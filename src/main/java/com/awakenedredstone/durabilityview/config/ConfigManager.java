package com.awakenedredstone.durabilityview.config;

import com.awakenedredstone.durabilityview.DurabilityView;
import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

public class ConfigManager {

    private Config config;
    private final File configFile = FabricLoader.getInstance().getConfigDir().resolve("durability-view.json").toFile();

    public void loadOrCreateConfig() {
        try {
            Path dir = FabricLoader.getInstance().getConfigDir();
            if ((dir.toFile().exists() && dir.toFile().isDirectory()) || dir.toFile().mkdirs()) {
                if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
                    config = DurabilityView.GSON.fromJson(new FileReader(configFile), Config.class);
                } else if (!configFile.exists()) {
                    JsonHelper.writeJsonToFile(defaultConfig(), getConfigFile());
                    config = new Config();
                }
                return;
            }
            DurabilityView.LOGGER.debug("Configurations loaded");
        } catch (Exception exception) {
            DurabilityView.LOGGER.error("An error occurred when trying to load the configurations!", exception);
            return;
        }

        DurabilityView.LOGGER.error("Failed to load the configurations!");
    }

    public JsonObject defaultConfig() {
        return DurabilityView.GSON.toJsonTree(new Config()).getAsJsonObject();
    }

    public Config getConfig() {
        return config;
    }

    public void save() {
        JsonHelper.writeJsonToFile(DurabilityView.GSON.toJsonTree(config).getAsJsonObject(), configFile);
    }

    public File getConfigFile() {
        return configFile;
    }
}
