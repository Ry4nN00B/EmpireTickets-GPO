package me.ry4nn00b.ticketsgpo.Events;

import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConnect;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConstructs;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class createTicket extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {

        //Variables
        String message = e.getMessage().getContentRaw();

        if (message.startsWith("!GPO")) {

            if (!e.getAuthor().getId().equals("1299009553333223467")) return;
            List<Member> mentionedMembers = e.getMessage().getMentions().getMembers();

            //Variables
            String prefix = Main.prefix;
            Member client = mentionedMembers.get(0);

            ConfigManager manager = new ConfigManager();

            Category category = e.getGuild().getCategoryById(manager.getTicketCategoryID());

            //Permissions
            ArrayList<Permission> permissions = new ArrayList<>();
            permissions.add(Permission.VIEW_CHANNEL);
            permissions.add(Permission.MESSAGE_SEND);
            permissions.add(Permission.MESSAGE_HISTORY);

            //Get Data
            LocalDate actualDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd∕MM");
            String formattedDate = actualDate.format(formatter);

            //Channel's Information
            String channelName = "🛒┃" + client.getUser().getName() + "┃" + formattedDate;
            String topicGPOChannel = "🆔┃" + client.getUser().getIdLong() + "┃Grand Piece Online";

            //Get Tickets Amount
            int ticketsAmount = Integer.parseInt(manager.getTickets());
            int ticketsAmountAdd = ticketsAmount + 1;

            //GPO Ticket
            if(ticketsAmount < 25) {
                //SQL Register
                if(!SQLConstructs.hasMemberTable(client))
                    SQLConstructs.addMemberTable(client);

                if (SQLConstructs.getTicket(client, "gpoTicket").equals("false")) {

                    manager.setTickets(String.valueOf(ticketsAmountAdd));
                    category.createTextChannel(channelName).setTopic(topicGPOChannel).addMemberPermissionOverride(client.getUser().getIdLong(), permissions, null).queue();
                    SQLConstructs.setTicket(client, "gpoTicket", "true");
                    e.getChannel().sendMessage(prefix + "Seu ticket foi gerado com sucesso!").queue(message1 -> message1.delete().queue());

                } else e.getChannel().sendMessage(prefix + "Você já possui um ticket nesta categoria!").queue(message1 -> message1.delete().queueAfter(1, TimeUnit.SECONDS));
            }else e.getChannel().sendMessage(prefix + "Já existem muitos encomendas de GPO ativa. Tente novamente mais tarde!").queue(message1 -> message1.delete().queueAfter(2, TimeUnit.SECONDS));
        }

    }

}
