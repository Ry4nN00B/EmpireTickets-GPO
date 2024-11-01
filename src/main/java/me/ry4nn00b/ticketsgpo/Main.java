package me.ry4nn00b.ticketsgpo;

import com.mercadopago.MercadoPagoConfig;
import me.ry4nn00b.ticketsgpo.Commands.closeTicket;
import me.ry4nn00b.ticketsgpo.Commands.pay;
import me.ry4nn00b.ticketsgpo.Commands.pix;
import me.ry4nn00b.ticketsgpo.Commands.resetMember;
import me.ry4nn00b.ticketsgpo.Events.*;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.SQLite.SQLConnect;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.internal.utils.JDALogger;

import java.util.EnumSet;

public class Main {

    public static String prefix = "**Empire Tickets |** ";
    public static JDA jda;

    public static void main(String[] args) throws InterruptedException {

        startUP();
        System.out.println(prefix + "Aplicação iniciada e pronto para uso!");

    }

    private static void startUP() throws InterruptedException {

        //Variables
        ConfigManager manager = new ConfigManager();

        //Get Tokens
        String discordToken = manager.getDiscordToken();
        String mpToken = manager.getMPToken();

        if(discordToken == null || mpToken == null)
            System.out.println(prefix + "Algum dos tokens está vazio, verifique e tente novamente!");

        //Initialize
        jda = JDABuilder.create(discordToken, EnumSet.allOf(GatewayIntent.class)).build();
        jda.getPresence().setActivity(Activity.playing("Grand Piece Online"));
        JDALogger.setFallbackLoggerEnabled(true);
        jda.awaitReady();
        System.out.println(prefix + "A aplicação está sendo iniciada...");

        //Mercado Pago Connection
        MercadoPagoConfig.setAccessToken(mpToken);
        System.out.println(prefix + "Mercado pago registrado e carregado com sucesso!");

        //Events Register
        getEvents();
        System.out.println(prefix + "Eventos registrados e carregados com sucesso!");

        //Commands Register
        getCommands();
        System.out.println(prefix + "Comandos registrados e carregados com sucesso!");

        //SQLite Connection
        SQLConnect.openConnection();
        System.out.println(prefix + "Database registrada e carregada com sucesso!");

    }

    private  static void getEvents(){

        jda.addEventListener(
                new closeTicket(), new pay(), new pix(), new resetMember(), new clientLeave(), new me.ry4nn00b.ticketsgpo.Events.closeTicket(),
                new createdTicket(), new createTicket(), new functionsTicket(), new pixCheck());

    }

    private static void getCommands() throws InterruptedException {

        CommandListUpdateAction command = jda.awaitReady().updateCommands();

        command.addCommands(Commands.slash("fechar-ticket-gpo", "Utilizado para fechar tickets do GPO.")).queue();
        command.addCommands(Commands.slash("pago-gpo", "Utilizado para confirmar entregas do GPO.")
                .addOption(OptionType.USER, "cliente", "Mencione o cliente", true)
                .addOption(OptionType.ATTACHMENT, "imagem", "Anexe uma imagem", false)).queue();
        command.addCommands(Commands.slash("pix-gpo", "Utilizado para gerar pagamentos do GPO.")
                .addOption(OptionType.NUMBER, "preco", "Digite o preço | Ex: 10,00", true)).queue();
        command.addCommands(Commands.slash("resetar-membro-gpo", "Utilizado para resetar membros do tikcet de GPO.")
                .addOption(OptionType.USER, "membro", "Mencione um membro", true)).queue();

    }

}