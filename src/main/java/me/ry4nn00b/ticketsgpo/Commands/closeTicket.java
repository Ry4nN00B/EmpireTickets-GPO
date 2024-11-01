package me.ry4nn00b.ticketsgpo.Commands;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class closeTicket extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {

        //Variables
        String prefix = Main.prefix;
        Member member = e.getMember();
        String channelName = e.getChannel().getName();
        String topicChannel = e.getChannel().asTextChannel().getTopic();

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

        //Command
        if(e.getName().equals("fechar-ticket-gpo")){
            if (member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(adminRole) || member.getRoles().contains(modRole) || member.getRoles().contains(supportRole) || member.getRoles().contains(devRole)) {
                if(channelName.contains("🛒┃") || channelName.contains("⌛┃") || channelName.contains("✅┃")) {
                    if(topicChannel.contains("┃Grand Piece Online")) {
                        e.replyEmbeds(MessagesManager.closeTicket()).setActionRow(
                                Button.success("closeGPOTicketConfirm", "✔\uFE0F Confirmar"),
                                Button.danger("closeGPOTicketCancel", "❌ Cancelar")
                        ).queue();
                    } else e.reply(prefix + "Este ticket não pertence a categoria de GPO!").setEphemeral(true).queue();
                }else e.reply(prefix + "Este canal não pertence a um ticket!").setEphemeral(true).queue();
            }else e.reply(prefix + "Essa interação esta disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }

    }

}
