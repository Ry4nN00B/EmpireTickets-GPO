package me.ry4nn00b.ticketsgpo.Events;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import me.ry4nn00b.ticketsgpo.Main;
import me.ry4nn00b.ticketsgpo.Managers.ConfigManager;
import me.ry4nn00b.ticketsgpo.Managers.MessagesManager;
import me.ry4nn00b.ticketsgpo.Managers.QRPixGenerator;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class pixCheck extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {

        //Variables
        String prefix = Main.prefix;
        String channelName = e.getChannel().getName();

        //Pix Cancel
        if(e.getButton().getId().equals("cancelGPOPix")) {
            //Information's
            String topicChannel = e.getChannel().asTextChannel().getTopic();

            HashMap<Member, Long> gpoTransaction = QRPixGenerator.gpoTransaction;
            HashMap<Member, String> gpoQRCode = QRPixGenerator.gpoQRCode;

            //Get Client
            String clientID = topicChannel.replace("🆔┃", "").replace("┃Grand Piece Online", "").trim();
            Member client = e.getGuild().getMemberById(clientID);

            if(gpoTransaction.containsKey(client)){
                e.deferReply().queue();

                try {
                    //Payment
                    File file = new File(client.getUser().getName() + "-GPO.png");
                    long transactionID = gpoTransaction.get(client);
                    PaymentClient paymentClient = new PaymentClient();

                    paymentClient.cancel(transactionID);
                    gpoTransaction.remove(client);
                    gpoQRCode.remove(client);
                    file.delete();

                    //Channel Name
                    if (channelName.contains("⌛"))
                        e.getChannel().asTextChannel().getManager().setName(channelName.replace("⌛", "🛒")).queue();

                    e.getMessage().delete().queue();
                    e.getHook().sendMessage(prefix + "Seu pagamento foi cancelado.").queue(message -> message.delete().queue());
                    return;

                } catch (MPException | MPApiException ex) {
                    System.err.println(prefix + "Não foi possível cancelar o pagamento do ticket: " + channelName + ".");
                    e.getHook().sendMessage(prefix + "Não foi possível cancelar seu pagamento.").queue();
                    throw new RuntimeException(ex);
                }
            }

        }

        //GPO Pix Check
        if(e.getButton().getId().equals("checkGPOPix")){
            //Information's
            String topicChannel = e.getChannel().asTextChannel().getTopic();

            HashMap<Member, Long> gpoTransaction = QRPixGenerator.gpoTransaction;
            HashMap<Member, String> gpoQRCode = QRPixGenerator.gpoQRCode;

            //Get Client
            String clientID = topicChannel.replace("🆔┃", "").replace("┃Grand Piece Online", "").trim();
            Member client = e.getGuild().getMemberById(clientID);

            if(gpoTransaction.containsKey(client)){
                e.deferReply().queue();

                try {
                    //Get Payment
                    File file = new File(client.getUser().getName() + "-GPO.png");
                    long transactionID = gpoTransaction.get(client);
                    PaymentClient paymentClient = new PaymentClient();
                    Payment payment = paymentClient.get(transactionID);
                    String paymentStatus = payment.getStatus();
                    BigDecimal price = payment.getTransactionAmount();

                    if(paymentStatus.equalsIgnoreCase("approved")){

                        //Channel Name
                        if (channelName.contains("🛒"))
                            e.getChannel().asTextChannel().getManager().setName(channelName.replace("🛒", "✅")).queue();
                        if (channelName.contains("⌛"))
                            e.getChannel().asTextChannel().getManager().setName(channelName.replace("⌛", "✅")).queue();

                        e.getMessage().delete().queue();
                        e.getHook().sendMessageEmbeds(MessagesManager.pixGenerate(Double.parseDouble(String.valueOf(price)), String.valueOf(transactionID), "confirmPayment"), MessagesManager.deliveryTime()).queue();

                        gpoTransaction.remove(client);
                        gpoQRCode.remove(client);
                        file.delete();
                        return;

                    }else e.getHook().sendMessage(prefix + "Seu pagamento ainda não foi aprovado...").queue(message -> message.delete().queueAfter(3, TimeUnit.SECONDS));
                } catch (MPException | MPApiException ex) {
                    System.err.println(prefix + "Não foi possível verificar o pagamento do ticket: " + channelName + ".");
                    e.getHook().sendMessage(prefix + "Não foi possível verificar seu pagamento.").queue();
                    throw new RuntimeException(ex);
                }
            }

        }
    }
}
