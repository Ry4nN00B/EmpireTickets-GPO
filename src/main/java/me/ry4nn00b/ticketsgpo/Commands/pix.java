package me.ry4nn00b.ticketsgpo.Commands;

import com.google.zxing.WriterException;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import me.ry4nn00b.ticketsgpo.Managers.QRCodeGenerator;
import me.ry4nn00b.ticketsgpo.Managers.QRPixGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class pix extends ListenerAdapter {

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
        if(e.getName().equals("pix-gpo")) {
            if (member.getRoles().contains(allRole) || member.getRoles().contains(ownerRole) || member.getRoles().contains(subOwnerRole) || member.getRoles().contains(directorRole) || member.getRoles().contains(managerRole) || member.getRoles().contains(adminRole) || member.getRoles().contains(modRole) || member.getRoles().contains(supportRole) || member.getRoles().contains(devRole)) {
                if(channelName.contains("🛒┃") || channelName.contains("⌛┃") || channelName.contains("✅┃")) {
                    if (topicChannel.contains("┃Grand Piece Online")) {
                        //Information's
                        HashMap<Member, Long> gpoTransaction = QRPixGenerator.gpoTransaction;

                        double price = e.getOption("preco").getAsDouble();

                        //Get client
                        String clientID = topicChannel.replace("🆔┃", "").replace("┃Grand Piece Online", "").trim();
                        Member client = e.getGuild().getMemberById(clientID);

                        if (client == null) {
                            e.reply(prefix + "Não foi possível encontrar este cliente...").queue();
                            return;
                        }

                        //GPO Purchase
                        if (!gpoTransaction.containsKey(client)) {
                            e.deferReply().queue();

                            try {
                                //Information's
                                String pathName = client.getUser().getName() + "-GPO.png";
                                String priceConvert = String.format("%.2f", price);
                                double priceFinal = Double.parseDouble(priceConvert.replace(",", "."));

                                //Create Payment
                                String pixCode = QRPixGenerator.createQRPix(client, priceFinal);
                                BufferedImage qrCode = QRCodeGenerator.generateQRCodeImage(pixCode, 300, 300);
                                QRCodeGenerator.saveQRCodeImage(qrCode, pathName);

                                //Ticket Information's
                                String transactionID = String.valueOf(gpoTransaction.get(client));
                                FileUpload upload = gpoFile(client.getUser().getName());

                                //Channel name
                                if (channelName.contains("🛒"))
                                    e.getChannel().asTextChannel().getManager().setName(channelName.replace("🛒", "⌛")).queue();
                                if (channelName.contains("✅"))
                                    e.getChannel().asTextChannel().getManager().setName(channelName.replace("✅", "⌛")).queue();

                                //Message
                                e.getHook().sendMessageEmbeds(MessagesManager.pixGenerate(price, transactionID, "awaityPayment"), MessagesManager.sendProof())
                                        .addContent(gpoQRCode(client)).addFiles(upload)
                                        .setActionRow(
                                                Button.danger("cancelGPOPix", "❌ Cancelar"),
                                                Button.success("checkGPOPix", "\uD83D\uDD04 Verificar")
                                        ).queue();

                            } catch (MPException | MPApiException | IOException | WriterException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        e.reply(prefix + "Este cliente possui um pagamento pendente nesta categoria...").setEphemeral(true).queue();
                    } else e.reply(prefix + "Esse ticket não pertence a categoria de GPO!").setEphemeral(true).queue();
                }else e.reply(prefix + "Este canal não pertence a um ticket!").setEphemeral(true).queue();
            }else e.reply(prefix + "Essa interação está disponível apenas para nossa equipe!").setEphemeral(true).queue();
        }
    }

    //Getter payment information's
    private static FileUpload gpoFile(String name){
        File file = new File(name + "-GPO.png");
        return FileUpload.fromData(file);
    }
    private static String gpoQRCode(Member client){
        HashMap<Member, String> gpoQRCode = QRPixGenerator.gpoQRCode;
        return gpoQRCode.get(client);
    }

}
