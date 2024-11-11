package me.ry4nn00b.ticketsgpo.Events;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class createdTicket extends ListenerAdapter {

    @Override
    public void onChannelCreate(@NotNull ChannelCreateEvent e) {

        //Variables
        String channelName = e.getChannel().getName();

        ConfigManager manager = new ConfigManager();

        //Roles
        Role supportRole = e.getGuild().getRoleById(manager.getSupportRole());

        //Event
        if(channelName.contains("🛒┃")){
            //Information's
            String topicChannel = e.getChannel().asTextChannel().getTopic();

            if(topicChannel.contains("┃Grand Piece Online")){
                //Get client
                String clientID = topicChannel.replace("🆔┃", "").replace("┃Grand Piece Online", "").trim();
                Member client = e.getGuild().getMemberById(clientID);
                String ticketChannelID = String.valueOf(e.getChannel().getIdLong());

                SQLConstructs.setTicket(client, "gpoTicket", ticketChannelID);
                e.getChannel().asMessageChannel().sendMessageEmbeds(MessagesManager.createdGPO(client)).addContent(supportRole.getAsMention() + client.getAsMention())
                        .setActionRow(
                                Button.primary("assumeTicket", "\uD83D\uDCCD Assumir Ticket"),
                                Button.success("confirmGPOPurchase", "✔\uFE0F Aprovar"),
                                Button.danger("closeGPOTicket", "\uD83D\uDDD1\uFE0F Fechar carrinho")
                        ).queue();

            }
        }

    }
}
