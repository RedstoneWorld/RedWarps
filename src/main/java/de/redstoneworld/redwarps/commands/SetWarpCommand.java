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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetWarpCommand extends RedWarpCommand {

    public SetWarpCommand(RedWarps plugin) {
        super(plugin, "setwarp");
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Command can only be run by a player!");
            return true;
        }

        String name = args[0];
        if (plugin.getWarpManager().getWarp(name) != null) {
            plugin.sendMessage(sender, "warpAlreadyExists", "warpname", name);
            return true;
        }

        List<String> aliases = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));

        Warp warp = new Warp(
                name,
                aliases,
                ((Player) sender).getLocation(),
                ((Player) sender).getEyeLocation().getYaw(),
                ((Player) sender).getEyeLocation().getPitch()
        );

        plugin.getWarpManager().addWarp(warp);

        plugin.sendMessage(sender, "setSuccess", "warpname", warp.getName());
        return true;
    }
}
