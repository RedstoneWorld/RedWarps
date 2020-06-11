package de.redstoneworld.redwarps.commands;

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

import de.redstoneworld.redwarps.RedWarps;
import de.redstoneworld.redwarps.Warp;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UpdateWarpCommand extends RedWarpCommand {

    public UpdateWarpCommand(RedWarps plugin) {
        super(plugin, "updatewarp");
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }

        Warp warp = plugin.getWarpManager().getWarp(args[0]);
        if (warp == null || !warp.getName().equalsIgnoreCase(args[0])) {
            plugin.sendMessage(sender, "warpNotFound", "warpname", args[0]);
            return true;
        }

        if ("position".equalsIgnoreCase(args[1])) {
            if (args.length == 2) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Position can only be changed by a player!");
                    return true;
                }
                warp.update(((Player) sender).getLocation());
                warp.setYaw(((Player) sender).getEyeLocation().getYaw());
                warp.setPitch(((Player) sender).getEyeLocation().getPitch());
            } else if (args.length > 5) {
                warp.setWorldName(args[2]);
                try {
                    warp.setX(Double.parseDouble(args[3]));
                    warp.setY(Double.parseDouble(args[4]));
                    warp.setZ(Double.parseDouble(args[5]));
                    if (args.length > 7) {
                        warp.setYaw(Float.parseFloat(args[6]));
                        warp.setPitch(Float.parseFloat(args[7]));
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(e.getMessage());
                    return false;
                }
            }
        } else if ("message".equalsIgnoreCase(args[1])) {
            if (args.length > 2) {
                warp.setMessage(args[2]);
            } else {
                warp.setMessage(null);
            }
        } else if ("permission".equalsIgnoreCase(args[1])) {
            if (args.length < 3) {
                return false;
            }
            warp.setPermission(args[2]);
        }

        plugin.getWarpManager().saveWarp(warp);

        plugin.sendMessage(sender, "setSuccess", "warpname", warp.getName());
        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return super.complete(sender, args);
        } else if (args.length == 2) {
            return Arrays.asList("position", "message", "permission");
        }
        List<String> list = new ArrayList<>();
        if ("position".equalsIgnoreCase(args[1])) {
            if (sender instanceof Player) {
                if (args.length < 6) {
                    Block target = ((Player) sender).getTargetBlock(6);
                    if (target != null) {
                        switch (args.length) {
                            case 3:
                                list.add(String.valueOf(target.getX() + 0.5));
                                break;
                            case 4:
                                list.add(String.valueOf(target.getY() + 0.5));
                                break;
                            case 5:
                                list.add(String.valueOf(target.getZ() + 0.5));
                                break;
                        }
                    }
                } else if (args.length == 6) {
                    list.add(String.valueOf(Math.round(((Player) sender).getEyeLocation().getYaw())));
                } else if (args.length == 7) {
                    list.add(String.valueOf(Math.round(((Player) sender).getEyeLocation().getPitch())));
                }
            } else if (args.length == 3) {
                for (World world : plugin.getServer().getWorlds()) {
                    list.add(world.getName());
                }
            }
        } else if ("permission".equalsIgnoreCase(args[1])) {
            list.add("rwm.redwarps.warp.");
            Warp warp = plugin.getWarpManager().getWarp(args[0]);
            if (warp != null && warp.getName().equalsIgnoreCase(args[0])) {
                list.add("rwm.redwarps.warp." + warp.getName());
            }
        }
        return list;
    }
}
