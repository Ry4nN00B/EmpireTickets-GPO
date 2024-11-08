package me.ry4nn00b.ticketsgpo.Events;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.QRPixGenerator;
import me.ry4nn00b.ticketsgpo.Managers.TranscriptionManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class closeTicket extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {

        //Variables
        String prefix = Main.prefix;
        Member staff = e.getMember();

        ConfigManager manager = new ConfigManager();

        HashMap<Member, Long> gpoTransaction = QRPixGenerator.gpoTransaction;
        HashMap<Member, String> gpoQRCode = QRPixGenerator.gpoQRCode;

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

        //Delete ticket on client leave
        if(e.getButton().getId().equals("closeGPOTicketLeave")) {
            e.reply(prefix + "Este canal será deletado em 3 segundos...").queue();
            e.getChannel().delete().queueAfter(3, TimeUnit.SECONDS);
        }

        //Cancel Close Ticket
        if(e.getButton().getId().equals("closeGPOTicketCancel")){
            if (staff.getRoles().contains(allRole) || staff.getRoles().contains(ownerRole) || staff.getRoles().contains(subOwnerRole) || staff.getRoles().contains(directorRole) || staff.getRoles().contains(managerRole) || staff.getRoles().contains(adminRole) || staff.getRoles().contains(modRole) || staff.getRoles().contains(supportRole) || staff.getRoles().contains(devRole)) {

                e.reply(prefix + "O ticket permanecerá aberto mais um pouco...").queue(interactionHook -> interactionHook.deleteOriginal().queueAfter(3, TimeUnit.SECONDS));
                e.getMessage().delete().queue();

            }else e.reply(prefix + "Esta interação esta disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

        //Confirm Close Ticket
        if(e.getButton().getId().equals("closeGPOTicketConfirm")){
            if (staff.getRoles().contains(allRole) || staff.getRoles().contains(ownerRole) || staff.getRoles().contains(subOwnerRole) || staff.getRoles().contains(directorRole) || staff.getRoles().contains(managerRole) || staff.getRoles().contains(adminRole) || staff.getRoles().contains(modRole) || staff.getRoles().contains(supportRole) || staff.getRoles().contains(devRole)) {
                //Information's
                String ticketChannelID = String.valueOf(e.getChannelIdLong());
                String topicChannel = e.getChannel().asTextChannel().getTopic();

                //Robux Ticket
                if(topicChannel.contains("Grand Piece Online")) {
                    //Get Client
                    String clientID = topicChannel.replace("🆔┃", "").replace("┃Grand Piece Online", "").trim();
                    Member client = e.getGuild().getMemberById(clientID);

                    if(gpoTransaction.containsKey(client)) {
                        e.deferReply().queue();

                        try {
                            //Get Payment
                            File file = new File(client.getUser().getName() + "-GPO.png");
                            long transactionID = gpoTransaction.get(client);
                            PaymentClient paymentClient = new PaymentClient();

                            paymentClient.cancel(transactionID);
                            gpoTransaction.remove(client);
                            gpoQRCode.remove(client);
                            file.delete();

                            TranscriptionManager.saveAndSendMessages(client, staff, ticketChannelID);
                            SQLConstructs.setTicket(client, "gpoTicket", "false");
                            e.getHook().sendMessage(prefix + "O canal será deletado em 3 segundos...").queue();
                            e.getChannel().delete().queueAfter(3, TimeUnit.SECONDS);
                            return;

                        } catch (MPException | MPApiException ex) {
                            throw new RuntimeException(ex);
                        }
                    }else {
                        TranscriptionManager.saveAndSendMessages(client, staff, ticketChannelID);
                        SQLConstructs.setTicket(client, "gpoTicket", "false");
                        e.reply(prefix + "O canal será deletado em 3 segundos...").queue();
                        e.getChannel().delete().queueAfter(3, TimeUnit.SECONDS);
                    }
                }

                if(manager.getAssumeTicket(ticketChannelID) != null)
                    manager.remAssumeTicket(ticketChannelID);

            }else e.reply(prefix + "Esta interação esta disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

    }
}
