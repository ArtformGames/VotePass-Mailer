package com.artformgames.plugin.votepassmailer.conf;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;

public interface PluginMessages extends Configuration {

    interface COMMAND extends Configuration {

        ConfiguredMessageList<String> USAGE = ConfiguredMessageList.asStrings()
                .defaults(
                        "&e&lVotePassMailer &fAdmin Commands",
                        "&8#&f test &e<result> &e<receiver>",
                        "&8-&7 Test the mail sending.",
                        "&8#&f reload",
                        "&8-&7 Reload the configuration file."
                ).build();

        ConfiguredMessageList<String> NO_PERMISSION = ConfiguredMessageList.asStrings()
                .defaults("&c&lSorry! &fBut you dont have enough permissions to do that!")
                .build();


        ConfiguredMessageList<String> ONLY_PLAYER = ConfiguredMessageList.asStrings()
                .defaults("&c&lSorry! &fBut this command only can be executed by a player!")
                .build();

    }

    interface RELOAD extends Configuration {

        ConfiguredMessageList<String> START = ConfiguredMessageList.asStrings()
                .defaults("&fReloading the plugin configurations...")
                .build();

        ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                .defaults("&a&lSuccess! &fThe plugin configurations has been reloaded, cost &a%(time)&fms.")
                .params("time")
                .build();

        ConfiguredMessageList<String> FAILED = ConfiguredMessageList.asStrings()
                .defaults("&c&lFailed! &fThe plugin configurations failed to reload.")
                .build();

    }


    interface TEST extends Configuration {

        ConfiguredMessageList<String> TYPE_NOT_FOUND = ConfiguredMessageList.asStrings()
                .defaults("&fPlease supply the correct type! &e&l[approved, rejected, expired]")
                .build();

        ConfiguredMessageList<String> WRONG_MAIL = ConfiguredMessageList.asStrings()
                .defaults("&fPlease supply the correct mail address!")
                .build();
        ConfiguredMessageList<String> START = ConfiguredMessageList.asStrings()
                .defaults("&fTrying to send email to &e%(receiver) &f...")
                .params("receiver")
                .build();

        ConfiguredMessageList<String> SUCCESS = ConfiguredMessageList.asStrings()
                .defaults("&a&lSuccess! &fMail has been sent to &e%(receiver) , cost &a%(time)&fms.")
                .params("receiver", "time")
                .build();

        ConfiguredMessageList<String> FAILED = ConfiguredMessageList.asStrings()
                .defaults("&c&lFailed! &fPlease check the configuration and console logs.")
                .build();
    }


}

