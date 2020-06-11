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

import de.redstoneworld.redwarps.commands.DelWarpCommand;
import de.redstoneworld.redwarps.commands.RedWarpCommand;
import de.redstoneworld.redwarps.commands.ReloadWarpsCommand;
import de.redstoneworld.redwarps.commands.SetWarpCommand;
import de.redstoneworld.redwarps.commands.UpdateWarpCommand;
import de.redstoneworld.redwarps.commands.WarpCommand;
import de.themoep.minedown.MineDown;
import de.themoep.utils.lang.bukkit.LanguageManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class RedWarps extends JavaPlugin {

    private LanguageManager lang;
    private List<PluginCommand> commands = new ArrayList<>();

    private WarpManager warpManager = new WarpManager(this);

    public void onEnable() {
        loadConfig();
        registerCommand(new ReloadWarpsCommand(this));
        registerCommand(new WarpCommand(this));
        registerCommand(new SetWarpCommand(this));
        registerCommand(new DelWarpCommand(this));
        registerCommand(new UpdateWarpCommand(this));
    }

    /**
     * Register a plugin command executor
     * @param executor  The command executor
     */
    private void registerCommand(RedWarpCommand executor) {
        PluginCommand command = getCommand(executor.getName());
        if (command != null) {
            commands.add(command);
            command.setExecutor(executor);
        } else {
            getLogger().log(Level.WARNING, "No plugin command with name " + executor.getName() + " found!");
        }
    }

    /**
     * Load the config
     * @return whether or not the config was successfully loaded; false means an error occurred but that data was still reloaded!
     */
    public boolean loadConfig() {
        boolean totalSuccess = true;
        saveDefaultConfig();
        reloadConfig();

        lang = new LanguageManager(this, "languages", "", getConfig().getString("default-language"));

        for (PluginCommand command : commands) {
            command.setPermissionMessage(getText(getServer().getConsoleSender(), "noPermission", "permission", "<permission>"));
        }

        totalSuccess |= warpManager.load();
        return totalSuccess;
    }

    /**
     * Get a text string from the language config
     * @param sender        The sender to get the text for
     * @param key           The text key to get
     * @param replacements  Array with alternating placeholders and replacements
     * @return The text or an error message if not found
     */
    private String getText(CommandSender sender, String key, String... replacements) {
        return TextComponent.toLegacyText(getMessage(sender, key, replacements));
    }

    /**
     * Get a text component from the language config
     * @param sender        The sender to get the text for
     * @param key           The text key to get
     * @param replacements  Array with alternating placeholders and replacements
     * @return The text components or an error message if not found
     */
    private BaseComponent[] getMessage(CommandSender sender, String key, String... replacements) {
        return MineDown.parse(lang.getConfig(sender).get("prefix") + lang.getConfig(sender).get(key, replacements));
    }

    /**
     * Send a message from the language config
     * @param sender        The sender to send the text to
     * @param key           The text key to send
     * @param replacements  Array with alternating placeholders and replacements
     */
    public void sendMessage(CommandSender sender, String key, String... replacements) {
        sender.sendMessage(getMessage(sender, key, replacements));
    }

    /**
     * Get the manager of the warps
     * @return The WarpManager
     */
    public WarpManager getWarpManager() {
        return warpManager;
    }

}
