package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    private static final String CONFIG_FILE = "config.json";

    private final GamePanel gp;
    private final ObjectMapper mapper;

    public Config(GamePanel gp) {
        this.gp = gp;
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty-print JSON
    }

    /**
     * Saves the current configuration settings to a JSON file.
     */
    public void saveConfig() {
        GameConfig config = new GameConfig(
                gp.fullScreenOn,
                gp.music.volumeScale,
                gp.SE.volumeScale
        );

        try {
            mapper.writeValue(new File(CONFIG_FILE), config);
            LOGGER.info("Configuration saved successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save configuration to " + CONFIG_FILE, e);
        }
    }

    /**
     * Loads configuration settings from a JSON file. If the file does not exist or is malformed,
     * default settings are used.
     */
    public void loadConfig() {
        File file = new File(CONFIG_FILE);

        if (!file.exists()) {
            LOGGER.warning("Config file not found. Using defaults.");
            return;
        }

        try {
            GameConfig config = mapper.readValue(file, GameConfig.class);
            gp.fullScreenOn = config.fullscreen;
            gp.music.volumeScale = config.musicVolume;
            gp.SE.volumeScale = config.seVolume;

            LOGGER.info("Configuration loaded successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration from " + CONFIG_FILE, e);
        }
    }

    /**
     * A simple POJO that represents the configuration state.
     */
    public static class GameConfig {
        public boolean fullscreen;
        public int musicVolume;
        public int seVolume;

        // Jackson needs a no-arg constructor
        public GameConfig() {}

        public GameConfig(boolean fullscreen, int musicVolume, int seVolume) {
            this.fullscreen = fullscreen;
            this.musicVolume = musicVolume;
            this.seVolume = seVolume;
        }
    }
}
