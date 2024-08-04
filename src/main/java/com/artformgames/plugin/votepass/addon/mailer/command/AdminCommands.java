package com.artformgames.plugin.votepass.addon.mailer.command;

import cc.carm.lib.easyplugin.command.CommandHandler;
import com.artformgames.plugin.votepass.addon.mailer.command.admin.TestCommand;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginMessages;
import com.artformgames.plugin.votepass.addon.mailer.command.admin.ReloadCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AdminCommands extends CommandHandler {

    public AdminCommands(@NotNull JavaPlugin plugin) {
        super(plugin);
        registerSubCommand(new TestCommand(this, "test"));
        registerSubCommand(new ReloadCommand(this, "reload"));
    }

    @Override
    public boolean hasPermission(@NotNull CommandSender sender) {
        return sender.hasPermission("votepass.admin");
    }

    @Override
    public Void noArgs(CommandSender sender) {
        PluginMessages.COMMAND.USAGE.send(sender);
        return null;
    }

    @Override
    public Void noPermission(CommandSender sender) {
        PluginMessages.COMMAND.NO_PERMISSION.send(sender);
        return null;
    }

}
