package de.ladbukkit.actionbarcompass;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * The main class of this plugin.
 *
 * @author LADBukkit (Robin Eschbach)
 */
public class ActionBarCompass extends JavaPlugin {

    /**
     * The message config for this plugin.
     */
    private MessageConfig messageConfig;

    /**
     * The location config for this plugin.
     */
    private LocationConfig locationConfig;

    /**
     * The TrackRunner of this plugin.
     */
    private TrackRunner trackRunner;

    /**
     * Creates the config and registers the commands.
     * Also starts the scheduler for the compass.
     */
    @Override
    public void onEnable() {
        this.getDataFolder().mkdirs();

        try {
            this.messageConfig = new MessageConfig(new File(this.getDataFolder(), "messages.yml"), "/messages.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.locationConfig = new LocationConfig(new File(this.getDataFolder(), "locations.yml"));

        getCommand("track").setExecutor(new TrackCommand(this));

        this.trackRunner = new TrackRunner(this);
        this.trackRunner.runTaskTimer(this, 0, 5);
    }

    /**
     * @return The message config of the plugin.
     */
    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    /**
     * @return The location config of the plugin.
     */
    public LocationConfig getLocationConfig() {
        return locationConfig;
    }

    /**
     * @return The TrackRunner of the plugin.
     */
    public TrackRunner getTrackRunner() {
        return trackRunner;
    }
}
