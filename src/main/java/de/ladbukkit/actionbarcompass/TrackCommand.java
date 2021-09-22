package de.ladbukkit.actionbarcompass;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

/**
 * Handles the track command.
 * @author LADBukkit (Robin Eschbach)
 */
public class TrackCommand implements CommandExecutor {

    /**
     * The plugin this command executor belongs to.
     */
    private final ActionBarCompass plugin;

    /**
     * Creates the handler for the track command.
     * @param plugin The plugin this belongs to.
     */
    public TrackCommand(ActionBarCompass plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(plugin.getMessageConfig().get("command.console"));
            return true;
        }

        Player p = (Player) sender;
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("stop")) {
                if(plugin.getTrackRunner().isTracking(p.getUniqueId())) {
                    plugin.getTrackRunner().unsetTracking(p.getUniqueId());
                    p.sendMessage(plugin.getMessageConfig().get("command.stopTrack"));
                } else {
                    p.sendMessage(plugin.getMessageConfig().get("command.notTracking"));
                }
            } else {
                String path = args[0].toLowerCase();
                Location loc = plugin.getLocationConfig().getLocation(path);
                if(loc == null) {
                    p.sendMessage(plugin.getMessageConfig().get("command.trackNotFound"));
                } else {
                    plugin.getTrackRunner().setTracking(p.getUniqueId(), loc);
                    p.sendMessage(plugin.getMessageConfig().get("command.setTrack").replace("%track%", path));
                }
            }
        } else if(args.length == 2) {
            if(!p.hasPermission("actionbarcompass.admin")) {
                p.sendMessage(plugin.getMessageConfig().get("noPermission"));
            } else if(args[0].equalsIgnoreCase("add")) {
                String path = args[1].toLowerCase();
                if(plugin.getLocationConfig().getLocation(path) != null) {
                    p.sendMessage(plugin.getMessageConfig().get("command.trackExists"));
                } else {
                    try {
                        plugin.getLocationConfig().setLocation(path, p.getLocation());
                        plugin.getLocationConfig().save();
                        p.sendMessage(plugin.getMessageConfig().get("command.save"));
                    } catch (IOException e) {
                        // Cleanup not saved location
                        plugin.getLocationConfig().setLocation(path, null);
                        p.sendMessage(plugin.getMessageConfig().get("command.couldNotSave"));
                    }
                }
            } else if(args[0].equalsIgnoreCase("remove")) {
                String path = args[1].toLowerCase();
                Location loc = plugin.getLocationConfig().getLocation(path);
                if(loc == null) {
                    p.sendMessage(plugin.getMessageConfig().get("command.trackNotFound"));
                } else {
                    try {
                        plugin.getLocationConfig().setLocation(path, null);
                        plugin.getLocationConfig().save();
                        p.sendMessage(plugin.getMessageConfig().get("command.remove"));
                    } catch (IOException e) {
                        // Cleanup not removed location
                        plugin.getLocationConfig().setLocation(path, loc);
                        p.sendMessage(plugin.getMessageConfig().get("command.couldNotSave"));
                    }
                }
            }
        } else if(args.length == 4) {
            String path = args[1].toLowerCase();
            if(plugin.getLocationConfig().getLocation(path) != null) {
                p.sendMessage(plugin.getMessageConfig().get("command.trackExists"));
            } else {
                try {
                    Location loc = new Location(p.getWorld(), Double.parseDouble(args[2]), 0, Double.parseDouble(args[3]));
                    plugin.getLocationConfig().setLocation(path, loc);
                    plugin.getLocationConfig().save();
                    p.sendMessage(plugin.getMessageConfig().get("command.save"));
                } catch (IOException e) {
                    // Cleanup not saved location
                    plugin.getLocationConfig().setLocation(path, null);
                    p.sendMessage(plugin.getMessageConfig().get("command.couldNotSave"));
                } catch(NumberFormatException e) {
                    p.sendMessage(plugin.getMessageConfig().get("command.notANumber"));
                }
            }
        } else {
            p.sendMessage(plugin.getMessageConfig().get("command.help"));
            if(p.hasPermission("actionbarcompass.admin")) {
                p.sendMessage(plugin.getMessageConfig().get("command.helpadmin"));
            }
        }

        return true;
    }
}
