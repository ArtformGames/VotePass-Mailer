package com.artformgames.plugin.votepass.addon.mailer.listener;

import com.artformgames.plugin.votepass.addon.mailer.Main;
import com.artformgames.plugin.votepass.addon.mailer.conf.PluginConfig;
import com.artformgames.plugin.votepass.addon.mailer.manager.MailManager;
import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.api.data.request.RequestInformation;
import com.artformgames.plugin.votepass.game.api.event.RequestResultUpdatedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResultListener implements Listener {

    @EventHandler
    public void onResultUpdate(RequestResultUpdatedEvent event) {
        RequestInformation request = event.getRequest();

        int questionID = PluginConfig.LINKED_QUESTION.getNotNull();
        RequestAnswer answer = request.getContents().get(questionID);
        if (answer == null) {
            Main.severe("No matched question with ID #" + questionID + " ! Email notify skipped.");
            return;
        }

        String answers = String.join(" ", answer.answers());
        String receiverEmail = MailManager.findEmail(answers);
        if (receiverEmail == null) {
            Main.debugging("No email address found in the answer: " + answers);
            return;
        }

        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("player", request.getUser().name());
        placeholders.put("request", request.getID());
        placeholders.put("create_time", PluginConfig.DATETIME_FORMATTER.format(request.getCreateTime()));
        placeholders.put("closed_time", PluginConfig.DATETIME_FORMATTER.format(LocalDateTime.now()));

        MailManager.sendEmail(receiverEmail, event.getResult(), placeholders);
    }


}
