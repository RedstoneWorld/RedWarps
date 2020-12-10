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

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Warp {
    private String name;
    private List<String> aliases;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean hidden;
    private String permission;
    private String message;
    private List<String> commands;

    public Warp(String name, List<String> aliases, String worldName, double x, double y, double z, float yaw, float pitch, boolean hidden, String permission, String message, List<String> commands) {
        this.name = name;
        this.aliases = new ArrayList<>(aliases);
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.hidden = hidden;
        this.permission = permission;
        this.message = message;
        this.commands = new ArrayList<>(commands);
    }

    public Warp(String name, List<String> aliases, Location location, float yaw, float pitch) {
        this(
                name,
                aliases,
                location.getWorld().getName(),
                location.getX(),
                location.getY(),
                location.getZ(),
                yaw,
                pitch,
                false,
                "rwm.redwarps.warp." + name,
                "",
                Collections.emptyList()
        );
    }

    /**
     * Create a new Warp object from a config section; section should have the name of the warp as its name
     * @param config The config section
     * @return The new Warp
     * @throws IllegalArgumentException if the name, world, x, y or z values aren't set
     */
    public static Warp fromConfig(ConfigurationSection config) {
        Validate.notNull(config.getName(), "Config has not name!");
        Validate.notNull(config.getString("world"), "Config has no world!");
        Validate.isTrue(config.contains("x"), "Config has no x value!");
        Validate.isTrue(config.contains("y"), "Config has no y value!");
        Validate.isTrue(config.contains("z"), "Config has no z value!");

        return new Warp(
                config.getName(),
                config.getStringList("aliases"),
                config.getString("world"),
                config.getDouble("x"),
                config.getDouble("y"),
                config.getDouble("z"),
                (float) config.getDouble("yaw", 0),
                (float) config.getDouble("pitch", 0),
                config.getBoolean("hidden", false),
                config.getString("permission", null),
                config.getString("message", ""),
                config.getStringList("commands")
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getCommands() {
        return commands;
    }

    /**
     * Directly get the World object corresponding to the warp
     * @return The World object; null if unloaded
     */
    public World getWorld() {
        return Bukkit.getWorld(getWorldName());
    }

    /**
     * Directly get the Location object corresponding to the warp
     * @return The Location object
     */
    public Location getLocation() {
        return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());
    }

    public void update(Location location) {
        setWorldName(location.getWorld().getName());
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
    }
}
