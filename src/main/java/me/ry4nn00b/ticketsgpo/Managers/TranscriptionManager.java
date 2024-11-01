package me.ry4nn00b.ticketsgpo.Managers;

import me.ry4nn00b.ticketsgpo.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class TranscriptionManager {

    public static void saveAndSendMessages(Member client, Member staff, String ticketChannelID) {

        //Variables
        JDA jda = Main.jda;
        ConfigManager manager = new ConfigManager();

        TextChannel ticketChannel = jda.getTextChannelById(ticketChannelID);
        TextChannel destinationChannel = jda.getTextChannelById(manager.getLogChannel());

        String prefix = Main.prefix;

        if (ticketChannel == null || destinationChannel == null) {
            System.err.println(prefix + "Um dos canais definidos não existe.");
            return;
        }

        //Transcription
        List<Message> messages = new LinkedList<>();
        collectMessages(ticketChannel, messages, null, () -> {
            try {
                List<Message> reversedMessages = new LinkedList<>();
                for (int i = messages.size() - 1; i >= 0; i--) {
                    reversedMessages.add(messages.get(i));
                }

                File file = new File(client.getUser().getName() + "-transcription.html");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("<html><head><style>" +
                            "body { font-family: Whitney, 'Helvetica Neue', Helvetica, Arial, sans-serif; margin: 0; padding: 20px; background-color: #36393f; color: #dcddde; }" +
                            ".container { max-width: 800px; margin: auto; }" +
                            ".message { display: flex; align-items: flex-start; margin-bottom: 20px; }" +
                            ".avatar { width: 40px; height: 40px; border-radius: 50%; margin-right: 15px; }" +
                            ".content { background-color: transparent; border-radius: 8px; padding: 0; width: 100%; }" +
                            ".author { font-weight: bold; color: #7289da; display: inline-block; }" +
                            ".timestamp { font-size: 12px; color: #72767d; margin-left: 10px; display: inline-block; }" +
                            ".message-content { margin-bottom: 10px; margin-left: 55px; }" +
                            ".embed { border-left: 4px solid #7289da; padding: 10px; background-color: #2f3136; border-radius: 8px; margin-top: 10px; }" +
                            ".embed-title { font-weight: bold; margin-bottom: 5px; }" +
                            ".mention { color: #7289da; }" +
                            "img { max-width: 100%; border-radius: 5px; margin-top: 10px; }" +
                            "a { color: #7289da; text-decoration: none; }" +
                            "a:hover { text-decoration: underline; }" +
                            "</style></head><body><div class='container'>");

                    for (Message message : reversedMessages) {
                        String timestamp = message.getTimeCreated().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"));

                        writer.write("<div class='message'>");
                        writer.write("<img class='avatar' src='" + message.getAuthor().getAvatarUrl() + "' alt='avatar'>");
                        writer.write("<div class='content'>");
                        writer.write("<div class='author'>" + message.getAuthor().getEffectiveName() + "</div>");
                        writer.write("<span class='timestamp'>" + timestamp + "</span>");
                        writer.write("<div class='message-content'>");

                        // Replace mentions with Discord-like formatting
                        String content = message.getContentDisplay()
                                .replaceAll("@([a-zA-Z0-9_]+)", "<span class='mention'>@$1</span>")
                                .replaceAll("#([a-zA-Z0-9_]+)", "<span class='mention'>#$1</span>");

                        writer.write("<p>" + content + "</p>");
                        writer.write("</div>");

                        if (!message.getEmbeds().isEmpty()) {
                            for (MessageEmbed embed : message.getEmbeds()) {
                                writer.write("<div class='embed'>");
                                if (embed.getTitle() != null) {
                                    writer.write("<div class='embed-title'>" + embed.getTitle() + "</div>");
                                }
                                if (embed.getDescription() != null) {
                                    writer.write("<p>" + embed.getDescription() + "</p>");
                                }
                                if (embed.getImage() != null) {
                                    writer.write("<img src='" + embed.getImage().getUrl() + "' alt='embed image'>");
                                }
                                if (embed.getThumbnail() != null) {
                                    writer.write("<img src='" + embed.getThumbnail().getUrl() + "' alt='embed thumbnail'>");
                                }
                                if (embed.getFooter() != null) {
                                    writer.write("<p><em>" + embed.getFooter().getText() + "</em></p>");
                                }
                                writer.write("</div>");
                            }
                        }

                        message.getAttachments().forEach(attachment -> {
                            try {
                                if (attachment.isImage()) {
                                    writer.write("<img src=\"" + attachment.getUrl() + "\" alt=\"image\" /><br>");
                                } else {
                                    writer.write("<a href=\"" + attachment.getUrl() + "\" style='color: #7289da;'>" + attachment.getFileName() + "</a><br>");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                        writer.write("</div></div>");
                    }

                    writer.write("</div></body></html>");
                }

                destinationChannel.sendFiles(FileUpload.fromData(file)).queue(message -> {
                    String transcriptionURL = message.getAttachments().get(0).getUrl();
                    destinationChannel.sendMessageEmbeds(MessagesManager.ticketLog(client, staff, transcriptionURL)).queue();
                    file.delete();
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public static void collectMessages(TextChannel channel, List<Message> messages, Message lastMessage, Runnable callback) {
        if (lastMessage == null) {
            channel.getHistory().retrievePast(100).queue(retrievedMessages -> {
                if (retrievedMessages.isEmpty()) {
                    callback.run();
                    return;
                }

                messages.addAll(retrievedMessages);
                collectMessages(channel, messages, retrievedMessages.get(retrievedMessages.size() - 1), callback);
            });
        } else {
            channel.getHistoryBefore(lastMessage, 100).queue(retrievedMessages -> {
                List<Message> retrievedMessagess = retrievedMessages.getRetrievedHistory();

                if (retrievedMessages.isEmpty()) {
                    callback.run();
                    return;
                }

                messages.addAll(retrievedMessagess);
                collectMessages(channel, messages, retrievedMessages.getRetrievedHistory().get(retrievedMessages.size() - 1), callback);
            });
        }
    }

}
