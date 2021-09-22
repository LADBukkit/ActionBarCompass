package de.ladbukkit.actionbarcompass;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * A config saving the locations of the tracks.
 * @author LADBukkit (Robin Eschbach)
 */
public class LocationConfig {
    /**
     * The file of the config.
     */
    private final File file;

    /**
     * The underlying config.
     */
    private final FileConfiguration config;

    /**
     * Creates the config at the specific file and loads it.
     * @param file The file.
     */
    public LocationConfig(File file) {
        this.file = file;
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Saves the config.
     * @throws IOException If the config could not be saved.
     */
    public void save() throws IOException {
        this.config.save(file);
    }

    /**
     * Gets the location at the specified path.
     * @param path The path of the location.
     * @return The location.
     */
    public Location getLocation(String path) {
        String locStr = this.config.getString(path);
        return locStr == null ? null : stringToLocation(locStr);
    }

    /**
     * Sets the location at the path.
     * This method does not save.
     * @param path The path of the location.
     * @param loc The location.
     */
    public void setLocation(String path, Location loc) {
        this.config.set(path, locationToString(loc));
    }

    /**
     * Converts a string to a location.
     * @param str The string.
     * @return The location.
     */
    private Location stringToLocation(String str) {
        String[] split = str.split(" ");
        return new Location(Bukkit.getWorld(split[0]),
                            Double.parseDouble(split[1]),
                            Double.parseDouble(split[2]),
                            Double.parseDouble(split[3]),
                            Float.parseFloat(split[4]),
                            Float.parseFloat(split[5]));
    }

    /**
     * Converts a location to a string.
     * @param loc The location.
     * @return The string.
     */
    private String locationToString(Location loc) {
        if(loc == null) return null;
        return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
    }
}
