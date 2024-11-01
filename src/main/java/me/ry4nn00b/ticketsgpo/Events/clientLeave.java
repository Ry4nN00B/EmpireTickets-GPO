package me.ry4nn00b.ticketsgpo.Events;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import me.ry4nn00b.ticketsgpo.Managers.QRPixGenerator;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

public class clientLeave extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent e) {

        //Variables
        Member client = e.getMember();

        ConfigManager manager = new ConfigManager();

        //Roles
        Role supportRole = e.getGuild().getRoleById(manager.getSupportRole());

        //Event
        if(!SQLConstructs.hasMemberTable(client))
            return;

        //GPO Ticket
        if(!SQLConstructs.getTicket(client, "gpoTicket").equals("false")){

            //Ticket Channel
            String ticketChannelID = SQLConstructs.getTicket(client, "gpoTicket");
            TextChannel ticketChannel = e.getGuild().getTextChannelById(ticketChannelID);

            //Information's
            HashMap<Member, Long> gpoTransaction = QRPixGenerator.gpoTransaction;
            HashMap<Member, String> gpoQRCode = QRPixGenerator.gpoQRCode;

            if(gpoTransaction.containsKey(client)){
                try {
                    //Get Payment
                    File file = new File(client.getUser().getName() + "-GPO.png");
                    long transactionID = gpoTransaction.get(client);
                    PaymentClient paymentClient = new PaymentClient();

                    SQLConstructs.setTicket(client, "gpoTicket", "false");
                    paymentClient.cancel(transactionID);
                    gpoTransaction.remove(client);
                    gpoQRCode.remove(client);
                    file.delete();

                    ticketChannel.sendMessageEmbeds(MessagesManager.clientLeave(client)).addContent(supportRole.getAsMention())
                            .setActionRow(Button.danger("closeGPOTicketLeave", "\uD83D\uDDD1\uFE0F Deletar Ticket")).queue();

                } catch (MPException | MPApiException ex) {
                    throw new RuntimeException(ex);
                }
            }else {

                SQLConstructs.setTicket(client, "gpoTicket", "false");
                ticketChannel.sendMessageEmbeds(MessagesManager.clientLeave(client)).addContent(supportRole.getAsMention())
                        .setActionRow(Button.danger("closeGPOTicketLeave", "\uD83D\uDDD1\uFE0F Deletar Ticket")).queue();

            }

        }

    }
}
