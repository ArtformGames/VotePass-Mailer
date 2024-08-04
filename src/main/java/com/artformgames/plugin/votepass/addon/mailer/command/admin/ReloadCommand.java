package com.artformgames.plugin.votepass.addon.mailer.command.admin;

import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.addon.mailer.Main;
import com.artformgames.plugin.votepass.addon.mailer.command.AdminCommands;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginMessages;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends SubCommand<AdminCommands> {

    public ReloadCommand(@NotNull AdminCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {

        try {
            PluginMessages.RELOAD.START.send(sender);
            long s1 = System.currentTimeMillis();
            Main.getInstance().getConfiguration().reload();
            PluginMessages.RELOAD.SUCCESS.send(sender, System.currentTimeMillis() - s1);
        } catch (Exception e) {
            PluginMessages.RELOAD.FAILED.send(sender);
            e.printStackTrace();
        }

        return null;
    }

}
