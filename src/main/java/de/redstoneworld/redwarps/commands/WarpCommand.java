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
import de.themoep.minedown.MineDown;
import de.themoep.minedown.Replacer;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class WarpCommand extends RedWarpCommand {

    public WarpCommand(RedWarps plugin) {
        super(plugin, "warp");
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }
        boolean silent = args.length > 1 && "-s".equalsIgnoreCase(args[1]);

        Warp warp = plugin.getWarpManager().getWarp(args[0]);
        if (warp == null) {
            sendMessage(sender, silent, "warpNotFound", "input", args[0]);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be run by a player!");
            return true;
        }

        if (!sender.hasPermission(warp.getPermission())) {
            sendMessage(sender, silent,"warpNoPermission", "warpname", warp.getName());
            return true;
        }

        if (warp.getWorld() == null) {
            sendMessage(sender, silent,"worldNotLoaded", "world", warp.getWorldName());
            return true;
        }

        Location warpLocation = warp.getLocation();

        warpLocation.getWorld().getChunkAtAsync(warpLocation).whenComplete((c, e) -> {
            if (e != null) {
                plugin.getLogger().log(Level.WARNING, "Unable to teleport " + sender.getName() + " to warp " + warp.getName() + " as the chunk wasn't properly loaded?", e);
            } else {
                Location belowLocation = warpLocation.clone().subtract(0, 1, 0);
                ((Player) sender).sendBlockChange(belowLocation, belowLocation.getBlock().getBlockData());
                ((Player) sender).teleport(warpLocation);
                if (!silent && warp.getMessage() != null && !warp.getMessage().isEmpty()) {
                    sender.sendMessage(MineDown.parse(warp.getMessage(), "warpname", warp.getName(), "prefix", plugin.getText(sender, "prefix")));
                } else {
                    sendMessage(sender, silent, "defaultWarpMessage", "warpname", warp.getName());
                }
                for (String command : warp.getCommands()) {
                    ((Player) sender).performCommand(Replacer.replaceIn(command, "warpname", warp.getName()));
                }
            }
        });

        return true;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length < 2) {
            for (Warp warp : plugin.getWarpManager().getWarps()) {
                if (!warp.isHidden() && (warp.getPermission() == null || sender.hasPermission(warp.getPermission()))) {
                    list.add(warp.getName());
                }
            }
        }
        return list;
    }
}
