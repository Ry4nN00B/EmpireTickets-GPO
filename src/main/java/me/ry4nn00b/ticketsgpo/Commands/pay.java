package me.ry4nn00b.ticketsgpo.Commands;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import me.ry4nn00b.ticketsgpo.Managers.TranscriptionManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class pay extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {

        //Variables
        String prefix = Main.prefix;
        Member member = e.getMember();

        ConfigManager manager = new ConfigManager();

        //Roles
        Role allRole = e.getGuild().getRoleById(manager.getAllRole());
        Role ownerRole = e.getGuild().getRoleById(manager.getOwnerRole());
        Role subOwnerRole = e.getGuild().getRoleById(manager.getSubOwnerRole());
        Role directorRole = e.getGuild().getRoleById(manager.getDirectorRole());
        Role managerRole = e.getGuild().getRoleById(manager.getManagerRole());
        Role adminRole = e.getGuild().getRoleById(manager.getAdminRole());
        Role modRole = e.getGuild().getRoleById(manager.getModRole());
        Role supportRole = e.getGuild().getRoleById(manager.getSupportRole());
        Role devRole = e.getGuild().getRoleById(manager.getDevRole());
        Role deliveryRole = e.getGuild().getRoleById(manager.getDeliveryRole());

        //Get Tickets Amount
        int ticketsAmount = Integer.parseInt(manager.getTickets());
        int ticketsAmountSub = ticketsAmount - 1;

        //Channels
        TextChannel deliveryLogChannel = e.getGuild().getTextChannelById(manager.getDeliveryLogChannelID());

        //Command
        if(e.getName().equals("pago-gpo")){
            if (member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(adminRole) || member.getRoles().contains(modRole) || member.getRoles().contains(supportRole) || member.getRoles().contains(deliveryRole) || member.getRoles().contains(devRole)) {
                //Information's
                String channelName = e.getChannel().getName();
                String topicChannel = e.getChannel().asTextChannel().getTopic();
                String ticketChannelID = String.valueOf(e.getChannelIdLong());

                if(channelName.contains("🛒┃") || channelName.contains("⌛┃") || channelName.contains("✅┃")) {
                    if (topicChannel.contains("┃Grand Piece Online")) {
                        //Information's
                        Member client = e.getOption("cliente").getAsMember();
                        String link = null;
                        Message.Attachment attachment;
                        if (e.getOption("imagem") != null) {
                            attachment = e.getOption("imagem").getAsAttachment();
                            link = attachment.getUrl();
                        }

                        //Messages
                        deliveryLogChannel.sendMessageEmbeds(MessagesManager.deliveryLogMessage(client, link)).queue();

                        manager.setTickets(String.valueOf(ticketsAmountSub));
                        SQLConstructs.setTicket(client, "gpoTicket", "false");
                        TranscriptionManager.saveAndSendMessages(client, member, ticketChannelID);
                        e.reply(prefix + "A entrega foi concluída! Vamos transcrever este ticket para segurança de todos!").queue();
                        e.getChannel().sendMessage("!leader-manager " + member.getAsMention()).queue(message -> message.delete().queueAfter(2, TimeUnit.SECONDS));
                        e.getChannel().delete().queueAfter(3, TimeUnit.SECONDS);
                    } else e.reply(prefix + "Este ticket não pertence a categoria de GPO!").setEphemeral(true).queue();
                }else e.reply(prefix + "Este canal não pertence a um ticket!").setEphemeral(true).queue();
            }else e.reply(prefix + "Essa interação esta disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

    }
}
