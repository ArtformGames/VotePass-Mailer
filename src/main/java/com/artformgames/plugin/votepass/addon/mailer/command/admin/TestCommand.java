package com.artformgames.plugin.votepass.addon.mailer.command.admin;

import cc.carm.lib.easyplugin.command.SimpleCompleter;
import cc.carm.lib.easyplugin.command.SubCommand;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginConfig;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginMessages;
import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepass.addon.mailer.command.AdminCommands;
import com.artformgames.plugin.votepass.addon.mailer.manager.MailManager;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCommand extends SubCommand<AdminCommands> {

    public TestCommand(@NotNull AdminCommands parent, String identifier, String... aliases) {
        super(parent, identifier, aliases);
    }

    @Override
    public Void execute(JavaPlugin plugin, CommandSender sender, String[] args) throws Exception {
        if (args.length != 2) return getParent().noArgs(sender);

        RequestResult result = Arrays.stream(RequestResult.values())
                .filter(s -> s.name().equalsIgnoreCase(args[0]))
                .findFirst().orElse(null);

        if (result == null || result == RequestResult.PENDING) {
            PluginMessages.TEST.TYPE_NOT_FOUND.send(sender);
            return null;
        }

        String mail = MailManager.findEmail(args[1]);
        if (mail == null) {
            PluginMessages.TEST.WRONG_MAIL.send(sender);
            return null;
        }

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("player", sender.getName());
        placeholders.put("request", "99999");
        placeholders.put("create_time", PluginConfig.DATETIME_FORMATTER.format(LocalDateTime.now()));
        placeholders.put("closed_time", PluginConfig.DATETIME_FORMATTER.format(LocalDateTime.now()));

        long s1 = System.currentTimeMillis();
        PluginMessages.TEST.START.send(sender, mail);
        MailManager.sendEmail(mail, result, placeholders).whenComplete((success, error) -> {
            if (success) {
                PluginMessages.TEST.SUCCESS.send(sender, mail, System.currentTimeMillis() - s1);
            } else {
                PluginMessages.TEST.FAILED.send(sender);
            }
        });

        return null;
    }

    @Override
    public List<String> tabComplete(JavaPlugin plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return SimpleCompleter.objects(args[0], Arrays.stream(RequestResult.values()).filter(r -> r != RequestResult.PENDING));
        } else return super.tabComplete(plugin, sender, args);
    }

}
