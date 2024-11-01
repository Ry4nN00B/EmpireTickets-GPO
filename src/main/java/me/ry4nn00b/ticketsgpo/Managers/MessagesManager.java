package me.ry4nn00b.ticketsgpo.Managers;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MessagesManager {

    public static MessageEmbed createdGPO(Member client){

        String message = """
                🚀 **Empire Tickets - Facilitando Suas Compras de Itens GPO!** 🚀

                🎉 **Obrigado por escolher a Empire Tickets para suas compras de GPO!**

                🔹 **Consulte os valore em <#1279901622457073746>**
                🔹 **Verifique seu nível antes de escolher um produto.**
                🔹 **Informe o produto desejado.**

                📞 *Se precisar de ajuda ou tiver dúvidas, nossa equipe de suporte está sempre pronta para ajudar!*

                💡 **Dica:** *Antes de enviar a solicitação, verifique seu inventário e se há espaço disponível para novos itens!*

                **Estamos aqui para garantir uma experiência rápida e tranquila para você. Conte conosco para as melhores compras em GPO! 🌊**
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");
        builder.setThumbnail(client.getUser().getAvatarUrl());
        builder.setImage("https://cdn.discordapp.com/attachments/1060312857633357846/1297988083341463763/SUP.png?ex=6717ed1a&is=67169b9a&hm=6c37afe5b65da107b5962795187c14d900c7c04dd871570a25ba5d455bf78835&");

        return builder.build();
    }
    public static MessageEmbed pixGenerate(double price, String transactionID, String option){

        //Convert Price
        NumberFormat format = NumberFormat.getInstance(new Locale("pt", "BR"));
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(2);
        String formattedPrice = format.format(price);

        //Embed
        String awaityPayment = """
                🌟 **Empire Tickets - Pagamento** 🌟
                
                ❖ **Pagamento - PIX**
                📌 **Nome do Beneficiário:** *Ingrid Borowski*
                📌 **Banco:** *Mercado Pago*
                🆔 **ID:** *||{transactionID}||*
                ⭕ **Status pagamento:** *⏳ Processando...*
                
                ⚠️ **Não aceitamos os bancos __INTER__ e __PicPay__**
                
                🛈 **Informações**
                💵 **Valor em Reais:** *R${moneyPrice}*
                """;

        String confirmPayment = """
                🌟 **Empire Tickets - Pagamento** 🌟
                
                ❖ **Pagamento - PIX**
                📌 **Nome do Beneficiário:** *Ingrid Borowski*
                📌 **Banco:** *Mercado Pago*
                🆔 **ID:** *||{transactionID}||*
                ⭕ **Status pagamento:** *✅ Aprovado!*
                
                🛈 **Informações**
                💵 **Valor em Reais:** *R${moneyPrice}*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        if(option.equals("awaityPayment"))
            builder.setDescription(awaityPayment
                    .replace("{moneyPrice}", formattedPrice)
                    .replace("{transactionID}", transactionID));
        else if(option.equals("confirmPayment"))
            builder.setDescription(confirmPayment
                    .replace("{moneyPrice}", formattedPrice)
                    .replace("{transactionID}", transactionID));

        return builder.build();
    }
    public static MessageEmbed sendProof(){

        String message = """
                📄 **| Envio de Comprovante Necessário!**
                **Para que possamos agilizar o processo do seu pedido, precisamos que você envie o comprovante de pagamento aqui neste canal.**

                ⚡ **Assim que recebermos o comprovante, nossa equipe iniciará a verificação e daremos continuidade ao seu pedido.**

                🛡️ **Agradecemos pela colaboração e confiança!**
                *Caso tenha dúvidas, estamos à disposição.*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        return builder.build();
    }
    public static MessageEmbed confirmPurchase(){

        String message = """
                🎉 **Empire Tickets - Compra Confirmada!** 🎉

                🛒 **Seu pagamento foi aprovado com sucesso!**
                *Você está a um passo de receber seu produto!*

                ✅ **Transação Concluída**
                *Seu pedido está sendo processado e será entregue em breve.*

                🔔 **Agradecemos pela sua compra!**
                *Continue sua jornada no jogo com estilo e não se esqueça de avaliar nosso serviço. Sua opinião é muito importante para nós!*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(Color.decode("#11f242"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        return builder.build();
    }
    public static MessageEmbed closeTicket(){

        String message = """
                ⚠️ **Empire Tickets - Confirmação Necessária** ⚠️

                *Ao confirmar, faremos a transcrição do ticket e, em seguida, o canal será excluído.*

                **Você tem certeza?**
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message);
        builder.setColor(Color.decode("#f2e311"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        return builder.build();
    }
    public static MessageEmbed ticketLog(Member client, Member staff, String transcriptionURL){

        String message = """
                🎟️ **Empire Tickets - Ticket Encerrado**

                🔒 **Este ticket foi oficialmente encerrado!**

                📌 **Cliente:** *{clientName}*
                🆔 **ID do Cliente:** *{clientID}*
                👨‍💼 **Encerrado por:** *{staffName}*

                📄 **Transcrição Completa do Ticket**
                *Confira o registro completo das mensagens trocadas no anexo. Para visualizar todos os detalhes, basta fazer o download do arquivo.*

                📥 **Transcrição:** *[Clique aqui para acessar]({transcriptionURL})*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message
                .replace("{clientName}", client.getAsMention())
                .replace("{clientID}", String.valueOf(client.getIdLong()))
                .replace("{staffName}", staff.getAsMention())
                .replace("{transcriptionURL}", transcriptionURL));
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        return builder.build();
    }
    public static MessageEmbed deliveryLogMessage(Member client, String link){

        String logWhitoutLink = """
                📑 **Empire Tickets - LOG's de Compra**

                🛒 **Nova Compra Registrada!**

                🔔 **Uma nova compra foi realizada!**

                📋 **Detalhes da Compra:**

                **Comprador:** *{clientName}*
                
                📦 **+1 Entrega Concluída!**
                *Mais um pedido foi entregue com sucesso. A equipe de entrega já finalizou o processo.*
                """;

        String logWhitLink = """
                📑 **Empire Tickets - LOG's de Compra**

                🛒 **Nova Compra Registrada!**

                🔔 **Uma nova compra foi realizada!**

                📋 **Detalhes da Compra:**

                **Comprador:** *{clientName}*
                **Produto Adquirido:** *[Clique para ver o produto]({link})*
                
                📦 **+1 Entrega Concluída!**
                *Mais um pedido foi entregue com sucesso. A equipe de entrega já finalizou o processo.*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        if (link != null && !link.isEmpty()) {
            builder.setDescription(logWhitLink
                    .replace("{clientName}", client.getAsMention())
                    .replace("{link}", link));
            builder.setImage(link);
        }else
            builder.setDescription(logWhitoutLink
                    .replace("{clientName}", client.getAsMention()));

        return builder.build();
    }
    public static MessageEmbed clientLeave(Member client){

        String message = """
                🚀 **Empire Tickets** 🚀

                🛈 **O cliente deixou o servidor!**

                🔧 **Ação Requerida:**
                📞 **Clique no botão abaixo para encerrar o ticket.**
                💡 **Dica:** *Revise o ticket antes de encerrá-lo para garantir que todos os detalhes foram resolvidos.*

                👤 **Nome do Cliente:** *{clientName}*
                🆔 **ID do Cliente:** *{clientID}*
                """;

        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(message
                .replace("{clientName}", client.getAsMention())
                .replace("{clientID}", String.valueOf(client.getUser().getIdLong())));
        builder.setColor(Color.decode("#2addf5"));
        builder.setFooter("Empire Tickets - Sua fonte confiável!");

        return builder.build();
    }

}
