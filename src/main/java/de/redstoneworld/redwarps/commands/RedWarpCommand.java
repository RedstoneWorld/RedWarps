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
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RedWarpCommand implements TabExecutor {
    protected final RedWarps plugin;
    private final String name;

    public RedWarpCommand(RedWarps plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!execute(sender, args) && !isSilent(args)) {
            if (plugin.hasMessage(sender, "usage." + getName())) {
                plugin.sendMessage(sender, "usage." + getName(), "alias", label);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        for (String completion : complete(sender, args)) {
            if (args.length == 0 || completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase())) {
                completions.add(completion);
            }
        }
        return completions;
    }

    /**
     * Tab completion handling for this command
     * @param sender    The sender to get completions for
     * @param args      The arguments in the input of the sender
     * @return The list of completions
     */
    public List<String> complete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    private boolean isSilent(String[] args) {
        for (String arg : args) {
            if ("-s".equalsIgnoreCase(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send a message to a sender
     * @param sender        Whom to send it to
     * @param silent        Whether or not the message should be send silently
     * @param key           The message key
     * @param replacements  Array with alternating placeholders and replacements
     */
    public void sendMessage(CommandSender sender, boolean silent, String key, String... replacements) {
        if (!silent) {
            plugin.sendMessage(sender, key, replacements);
        }
    }

    /**
     * What to do when the command is executed
     * @param sender    The sender who executed it
     * @param args      The input arguments
     * @return Whether or not the command was executed successfully
     */
    protected abstract boolean execute(CommandSender sender, String[] args);

}
