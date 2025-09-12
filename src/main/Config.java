package main;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Config {

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private final GamePanel gp;
    private static final String CONFIG_FILE = "config";

    public Config(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Saves the current configuration settings to a file.
     */
    public void saveConfig() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CONFIG_FILE))) {
            bw.write(gp.fullScreenOn ? "On" : "Off");
            bw.newLine();
            bw.write(String.valueOf(gp.music.volumeScale));
            bw.newLine();
            bw.write(String.valueOf(gp.SE.volumeScale));
            bw.newLine();

            LOGGER.info("Configuration saved successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save configuration to " + CONFIG_FILE, e);
        }
    }

    /**
     * Loads configuration settings from a file. If the file does not exist or is malformed,
     * default settings are used.
     */
    public void loadConfig() {
        File file = new File(CONFIG_FILE);

        if (!file.exists()) {
            LOGGER.warning("Config file not found. Using defaults.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String fullScreenSetting = br.readLine();
            if ("On".equalsIgnoreCase(fullScreenSetting)) {
                gp.fullScreenOn = true;
            } else if ("Off".equalsIgnoreCase(fullScreenSetting)) {
                gp.fullScreenOn = false;
            } else {
                LOGGER.warning("Invalid fullscreen setting in config: " + fullScreenSetting);
            }

            String musicVolume = br.readLine();
            String seVolume = br.readLine();

            if (musicVolume != null && seVolume != null) {
                gp.music.volumeScale = parseVolume(musicVolume, gp.music.volumeScale, "music");
                gp.SE.volumeScale = parseVolume(seVolume, gp.SE.volumeScale, "sound effects");
            } else {
                LOGGER.warning("Config file is incomplete. Some settings may use defaults.");
            }

            LOGGER.info("Configuration loaded successfully.");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration from " + CONFIG_FILE, e);
        }
    }

    private int parseVolume(String value, int defaultValue, String type) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid " + type + " volume value: " + value + ". Using default " + defaultValue, e);
            return defaultValue;
        }
    }
}
