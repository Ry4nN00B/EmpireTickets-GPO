package me.ry4nn00b.ticketsgpo.Managers;

import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import net.dv8tion.jda.api.entities.Member;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;

public class QRPixGenerator {

    public static HashMap<Member, Long> gpoTransaction = new HashMap<>();
    public static HashMap<Member, String> gpoQRCode = new HashMap<>();

    public static String createQRPix(Member client, double robuxPrice) throws MPException, MPApiException {

        //Products Description
        String gpoDescription = gpoDescription(client);

        //Expired Time
        OffsetDateTime expirationDateTime = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);

        //Payment
        PaymentClient clientPay = new PaymentClient();

        PaymentCreateRequest request = PaymentCreateRequest.builder()
                .transactionAmount(new BigDecimal(robuxPrice))
                .description(gpoDescription)
                .paymentMethodId("pix")
                .dateOfExpiration(expirationDateTime)
                .payer(PaymentPayerRequest.builder()
                        .email("pagador@example.com")
                        .build())
                .build();

        //Payment Information's
        Payment payment = clientPay.create(request);

        String qrcodeKey = payment.getPointOfInteraction().getTransactionData().getQrCode();
        long transactionID = payment.getId();

        gpoQRCode.put(client, qrcodeKey);
        gpoTransaction.put(client, transactionID);

        return qrcodeKey;

    }

    private static String gpoDescription(Member client){
        //Client Information
        String clientName = client.getUser().getName();
        long clientID = client.getUser().getIdLong();

        //Description
        String robuxDescription = "Empire Tickets - GPO | DiscordName: {clientName} / DiscordID: {clientID}";

        return robuxDescription.replace("{clientName}", clientName)
                .replace("{clientID}", String.valueOf(clientID));
    }

}
