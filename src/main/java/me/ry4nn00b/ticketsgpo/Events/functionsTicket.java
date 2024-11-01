package me.ry4nn00b.ticketsgpo.Events;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class functionsTicket extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {

        //Variables
        String prefix = Main.prefix;
        Member member = e.getMember();
        String channelName = e.getChannel().getName();

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

        //GPO Confirm Purchase Button
        if(e.getButton().getId().equals("confirmGPOPurchase")){
            if(member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(adminRole) || member.getRoles().contains(modRole) || member.getRoles().contains(supportRole) || member.getRoles().contains(devRole)) {
                if (e.getChannel().asTextChannel().getTopic().contains("┃Grand Piece Online")) {

                    //Channel name
                    if (channelName.contains("🛒┃"))
                        e.getChannel().asTextChannel().getManager().setName(channelName.replace("🛒┃", "✅┃")).queue();
                    if (channelName.contains("⌛┃"))
                        e.getChannel().asTextChannel().getManager().setName(channelName.replace("⌛┃", "✅┃")).queue();

                    //Message
                    e.getChannel().sendMessageEmbeds(MessagesManager.confirmPurchase()).queue();
                    e.reply(prefix + "A compra foi confirmada!").setEphemeral(true).queue();
                    return;

                }
            }else e.reply(prefix + "Esta interação está disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

        //GPO Close Ticket Button
        if(e.getButton().getId().equals("closeGPOTicket")) {
            if (member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(adminRole) || member.getRoles().contains(modRole) || member.getRoles().contains(supportRole) || member.getRoles().contains(devRole)) {
                if(e.getChannel().asTextChannel().getTopic().contains("┃Grand Piece Online")) {
                    e.replyEmbeds(MessagesManager.closeTicket()).setActionRow(
                            Button.success("closeGPOTicketConfirm", "✔\uFE0F Confirmar"),
                            Button.danger("closeGPOTicketCancel", "❌ Cancelar")
                    ).queue();
                    return;
                }
            }else e.reply(prefix + "Esta interação está disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

    }
}
