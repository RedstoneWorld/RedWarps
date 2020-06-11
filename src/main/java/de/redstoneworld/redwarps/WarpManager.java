package de.redstoneworld.redwarps;

/*
 * RedWarps
 * Copyright (c) 2020 Redstoneworld.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class WarpManager {
    private final RedWarps plugin;
    private ConfigAccessor warpsConfig;
    private Map<String, Warp> warps = new HashMap<>();

    public WarpManager(RedWarps plugin) {
        this.plugin = plugin;
        warpsConfig = new ConfigAccessor(plugin, "warps.yml");
    }

    /**
     * Load the warp manager data from the config
     * @return Whether or not the load finished without errors (doesn't mean that nothing loaded!)
     */
    public boolean load() {
        boolean totalSuccess = true;
        warps = new HashMap<>();

        warpsConfig.reloadConfig();
        for (String warpName : warpsConfig.getConfig().getKeys(false)) {
            try {
                Warp warp = Warp.fromConfig(warpsConfig.getConfig().getConfigurationSection(warpName));
                if (warp.getWorld() == null) {
                    plugin.getLogger().log(Level.WARNING, "World " + warp.getWorldName() + " of warp " + warp.getName() + " is unknown!");
                }
                warps.put(warp.getName().toLowerCase(), warp);
                for (String alias : warp.getAliases()) {
                    Warp existingWarp = warps.putIfAbsent(alias.toLowerCase(), warp);
                    if (existingWarp != null) {
                        plugin.getLogger().log(Level.WARNING,
                                "Could not assign alias " + alias + " to warp " + warp.getName() + " as a warp with that "
                                        + (existingWarp.getName().equalsIgnoreCase(alias) ? "name" : "alias (" + existingWarp.getName() + ")")
                                        + " already exists!");
                        totalSuccess = false;
                    }
                }
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.WARNING, "Could not load warp " + warpName + "! " + e.getMessage());
                totalSuccess = false;
            }
        }
        return totalSuccess;
    }

    /**
     * Get a warp by its name or alias
     * @param name The name or alias of the warp
     * @return The warp or null
     */
    public Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    /**
     * Get all defined warps
     * @return A collection with all warps
     */
    public Collection<Warp> getWarps() {
        return warps.values();
    }

    /**
     * Add a new warp to the manager and config
     * @param warp The warp to add
     */
    public void addWarp(Warp warp) {
        warps.put(warp.getName().toLowerCase(), warp);
        for (String alias : warp.getAliases()) {
            warps.putIfAbsent(alias.toLowerCase(), warp);
        }
        saveWarp(warp);
    }

    /**
     * Save all warp information to the warps.yml config
     * @param warp The warp to save
     */
    public void saveWarp(Warp warp) {
        ConfigurationSection section = warpsConfig.getConfig().createSection(warp.getName());
        section.set("aliases", warp.getAliases());
        section.set("world", warp.getWorldName());
        section.set("x", warp.getX());
        section.set("y", warp.getY());
        section.set("z", warp.getZ());
        section.set("yaw", warp.getYaw());
        section.set("pitch", warp.getPitch());
        section.set("permission", warp.getPermission());
        section.set("message", warp.getMessage());
        section.set("commands", warp.getCommands());
        warpsConfig.saveConfig();
    }

    /**
     * Remove a warp from the plugin and the config
     * @param warp The warp to remove
     */
    public void removeWarp(Warp warp) {
        warps.remove(warp.getName().toLowerCase(), warp);
        for (String alias : warp.getAliases()) {
            warps.remove(alias.toLowerCase(), warp);
        }
        warpsConfig.getConfig().set(warp.getName(), null);
        warpsConfig.saveConfig();
    }
}
