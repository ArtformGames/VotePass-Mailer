package com.artformgames.plugin.votepass.addon.mailer;

import cc.carm.lib.easyplugin.EasyPlugin;
import cc.carm.lib.easyplugin.i18n.EasyPluginMessageProvider;
import cc.carm.lib.easyplugin.utils.JarResourceUtils;
import cc.carm.lib.mineconfiguration.bukkit.MineConfiguration;
import com.artformgames.plugin.votepass.addon.mailer.command.AdminCommands;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginConfig;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginMessages;
import com.artformgames.plugin.votepass.addon.mailer.listener.ResultListener;
import com.artformgames.plugin.votepass.addon.mailer.util.GHUpdateChecker;
import org.bstats.bukkit.Metrics;

import java.io.File;
import java.io.IOException;

public class Main extends EasyPlugin {
    private static Main instance;

    public Main() {
        super(EasyPluginMessageProvider.EN_US);
        Main.instance = this;
    }

    protected MineConfiguration configuration;

    @Override
    protected void load() {

        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                JarResourceUtils.copyFolderFromJar("mail", getDataFolder(), JarResourceUtils.CopyOption.COPY_IF_NOT_EXIST);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        log("Loading plugin configurations...");
        this.configuration = new MineConfiguration(this);
        this.configuration.initializeConfig(PluginConfig.class);
        this.configuration.initializeMessage(PluginMessages.class);

    }

    @Override
    protected boolean initialize() {

        log("Register listeners...");
        registerListener(new ResultListener());

        log("Register commands...");
        registerCommand("VotePassMailer", new AdminCommands(this));

        if (PluginConfig.METRICS.getNotNull()) {
            log("Initializing bStats...");
            new Metrics(this, 21159);
        }

        if (PluginConfig.CHECK_UPDATE.getNotNull()) {
            log("Start to check the plugin versions...");
            getScheduler().runAsync(GHUpdateChecker.runner(this));
        } else {
            log("Version checker is disabled, skipped.");
        }

        return true;
    }

    @Override
    public boolean isDebugging() {
        return PluginConfig.DEBUG.getNotNull();
    }

    public MineConfiguration getConfiguration() {
        return configuration;
    }

    public static void info(String... messages) {
        getInstance().log(messages);
    }

    public static void severe(String... messages) {
        getInstance().error(messages);
    }

    public static void debugging(String... messages) {
        getInstance().debug(messages);
    }

    public static Main getInstance() {
        return instance;
    }

}