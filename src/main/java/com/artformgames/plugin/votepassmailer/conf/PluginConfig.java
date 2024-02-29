package com.artformgames.plugin.votepassmailer.conf;

import cc.carm.lib.configuration.core.Configuration;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.configuration.core.value.type.ConfiguredValue;

import java.time.format.DateTimeFormatter;

public interface PluginConfig extends Configuration {

    DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ConfiguredValue<Boolean> DEBUG = ConfiguredValue.of(Boolean.class, false);

    @HeaderComment({
            "Statistics Settings",
            "This option is used to help developers count plug-in versions and usage, and it will never affect performance and user experience.",
            "Of course, you can also choose to turn it off here for this plugin,",
            "or turn it off for all plugins in the configuration file under \"plugins/bStats\"."
    })
    ConfiguredValue<Boolean> METRICS = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "Check update settings",
            "This option is used by the plug-in to determine whether to check for updates.",
            "If you do not want the plug-in to check for updates and prompt you, you can choose to close.",
            "Checking for updates is an asynchronous operation that will never affect performance and user experience."
    })
    ConfiguredValue<Boolean> CHECK_UPDATE = ConfiguredValue.of(Boolean.class, true);

    @HeaderComment({
            "", "",
            "The question ID that contains users email address",
            "If user doesn't supply a valid email, no email will be sent to him."
    })
    ConfiguredValue<Integer> LINKED_QUESTION = ConfiguredValue.of(1);

    @HeaderComment({"Sender mail address."})
    ConfiguredValue<String> SENDER = ConfiguredValue.of("admin@exmaple.com");

    @HeaderComment({"Sender's name"})
    ConfiguredValue<String> PERSONAL = ConfiguredValue.of("ExampleServer <admin@exmaple.com>");

    ConfiguredValue<String> HOST = ConfiguredValue.of("smtp.example.com");

    ConfiguredValue<Integer> PORT = ConfiguredValue.of(445);

    @HeaderComment("0 = DEFAULT, 1 = with SSL, 2= with TLS/STARTTLS")
    ConfiguredValue<Integer> PROTOCOL = ConfiguredValue.of(1);

    @HeaderComment("Settings for email authenticator")
    interface AUTH extends Configuration {

        ConfiguredValue<Boolean> ENABLE = ConfiguredValue.of(true);

        ConfiguredValue<String> USERNAME = ConfiguredValue.of("admin@exmaple.com");

        ConfiguredValue<String> PASSWORD = ConfiguredValue.of("admin@exmaple.com");

    }

    @HeaderComment({
            "File path for email contents",
            "Current supported variables:",
            " - %(player) = The player's name",
            " - %(request) = The request's id",
            " - %(create_time) = The request's create time",
            " - %(closed_time) = The request's closed time"
    })
    interface CONTENTS extends Configuration {

        interface APPROVED extends Configuration {

            ConfiguredValue<String> SUBJECT = ConfiguredValue.of(
                    "Your whitelist request(#%(request)) was APPROVED ! | ServerName"
            );

            ConfiguredValue<String> FILE_PATH = ConfiguredValue.of("mail/approved-email.html");

        }

        interface REJECTED extends Configuration {

            ConfiguredValue<String> SUBJECT = ConfiguredValue.of(
                    "Your whitelist request(#%(request)) was REJECTED ! | ServerName"
            );

            ConfiguredValue<String> FILE_PATH = ConfiguredValue.of("mail/rejected-email.html");

        }


        interface EXPIRED extends Configuration {

            ConfiguredValue<String> SUBJECT = ConfiguredValue.of(
                    "Your whitelist request(#%(request)) was EXPIRED ! | ServerName"
            );

            ConfiguredValue<String> FILE_PATH = ConfiguredValue.of("mail/expired-email.html");

        }

    }

}