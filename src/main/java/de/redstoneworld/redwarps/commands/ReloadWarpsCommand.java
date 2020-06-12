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
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ReloadWarpsCommand extends RedWarpCommand {

    public ReloadWarpsCommand(RedWarps plugin) {
        super(plugin, "reloadwarps");
    }

    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            if (plugin.loadConfig()) {
                plugin.sendMessage(sender, "reload.success");
            } else {
                plugin.sendMessage(sender, "reload.failed");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
