package me.ry4nn00b.ticketsgpo.Commands;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class resetMember extends ListenerAdapter {

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
        Role devRole = e.getGuild().getRoleById(manager.getDevRole());

        //Command
        if(e.getName().equals("resetar-membro-gpo")) {
            if (member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(devRole)) {

                Member target = e.getOption("membro").getAsMember();

                SQLConstructs.setTicket(target, "gpoTicket", "false");

                e.reply(prefix + target.getAsMention() + " acabou de ser resetado.").setEphemeral(true).queue();
                System.out.println(prefix + target.getAsMention() + " acabou de ser resetado pelo staff " + member.getAsMention() + ".");

            }else e.reply(prefix + "Essa interação está disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }
    }

}
