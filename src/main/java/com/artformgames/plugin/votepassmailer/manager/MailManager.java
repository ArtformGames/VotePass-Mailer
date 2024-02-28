package com.artformgames.plugin.votepassmailer.manager;

import com.artformgames.plugin.votepass.api.data.request.RequestResult;
import com.artformgames.plugin.votepassmailer.Main;
import com.artformgames.plugin.votepassmailer.conf.PluginConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailManager {

    // Email pattern , to match an email address from a long text.
    private static final Pattern EMAIL_PATTERN = Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");

    private MailManager() {
    }

    public static CompletableFuture<Boolean> sendEmail(@NotNull String receiver, @NotNull RequestResult result,
                                                       @NotNull Map<String, Object> placeholders) {
        @NotNull String subject;
        @NotNull String contentPath;

        switch (result) {
            case EXPIRED -> {
                subject = PluginConfig.CONTENTS.EXPIRED.SUBJECT.getNotNull();
                contentPath = PluginConfig.CONTENTS.EXPIRED.FILE_PATH.getNotNull();
            }
            case APPROVED -> {
                subject = PluginConfig.CONTENTS.APPROVED.SUBJECT.getNotNull();
                contentPath = PluginConfig.CONTENTS.APPROVED.FILE_PATH.getNotNull();
            }
            case REJECTED -> {
                subject = PluginConfig.CONTENTS.REJECTED.SUBJECT.getNotNull();
                contentPath = PluginConfig.CONTENTS.REJECTED.FILE_PATH.getNotNull();
            }
            default -> {
                return CompletableFuture.completedFuture(false);
            }
        }

        File contentFile = new File(Main.getInstance().getDataFolder(), contentPath);
        return CompletableFuture.supplyAsync(() -> {
            Main.debugging("Trying to send email to " + receiver + " ...");
            return MailManager.sendEmail(createSession(), receiver, subject, readContent(contentFile, placeholders));
        });
    }

    public static @Nullable String findEmail(@NotNull String content) {
        Matcher matcher = EMAIL_PATTERN.matcher(content);
        if (!matcher.find()) return null;
        return matcher.group(1);
    }

    public static Session createSession() {
        Properties props = System.getProperties();

        String host = PluginConfig.HOST.getNotNull();
        String port = PluginConfig.PORT.getNotNull().toString();

        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        int protocolNum = PluginConfig.PROTOCOL.getNotNull();
        if (protocolNum == 1) {
            props.put("mail.smtp.socketFactory.port", port); //SSL Port
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
        } else if (protocolNum == 2) {
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        }

        Authenticator authenticator = null;
        if (PluginConfig.AUTH.ENABLE.getNotNull()) {
            props.put("mail.smtp.auth", "true"); //enable authentication
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                            PluginConfig.AUTH.USERNAME.getNotNull(),
                            PluginConfig.AUTH.PASSWORD.getNotNull()
                    );
                }
            };
        }

        return Session.getInstance(props, authenticator);
    }

    public static String readContent(@Nullable File contentFile, Map<String, Object> placeholders) {
        if (contentFile == null || !contentFile.exists()) {
            Main.severe("Failed to read email content file: " + contentFile);
            return null;
        }

        try (InputStream is = contentFile.toURI().toURL().openStream()) {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return setPlaceholders(new String(bytes), placeholders);
        } catch (Exception e) {
            Main.severe("Failed to read email content file: " + contentFile);
            e.printStackTrace();
            return null;

        }
    }

    public static boolean sendEmail(@NotNull Session session, @NotNull String receiver,
                                    @NotNull String subject, @Nullable String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(PluginConfig.SENDER.getNotNull(), PluginConfig.PERSONAL.getNotNull()));

            msg.setReplyTo(InternetAddress.parse(PluginConfig.SENDER.getNotNull(), false));

            msg.setSubject(subject, "UTF-8");

            msg.setContent(body == null ? "" : body, "text/html;charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver, false));
            Transport.send(msg);
            Main.debugging("Successfully send email to \"" + receiver + "\" [" + subject + "] ！");
            return true;
        } catch (Exception e) {
            Main.severe("Failed to send email to \"" + receiver + "\" [" + subject + "]");
            e.printStackTrace();
            return false;
        }
    }

    public static String setPlaceholders(@NotNull String messages, @NotNull Map<String, Object> placeholders) {
        if (messages.isEmpty()) return messages;

        String parsed = messages;
        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            Object value = entry.getValue();
            parsed = parsed.replace("%(" + entry.getKey() + ")", value == null ? "" : value.toString());
        }

        return parsed;
    }


}
