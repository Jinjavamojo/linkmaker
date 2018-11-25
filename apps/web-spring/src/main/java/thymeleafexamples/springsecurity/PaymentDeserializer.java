package thymeleafexamples.springsecurity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;
import thymeleafexamples.springsecurity.yandex.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

public class PaymentDeserializer extends StdDeserializer<Payment> {

    private Logger logger = Logger.getLogger(getClass().getName());

    public PaymentDeserializer() {
        this(null);
    }

    public PaymentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Payment deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Payment payment = new Payment();

        JsonNode amountNode = node.get("amount");
        Amount amount = new Amount();
        amount.setValue(amountNode.get("value").asDouble());
        amount.setCurrency(Currency.valueOf(amountNode.get("currency").asText().toUpperCase())); //TODO MAY BE ERROR
        payment.setAmount(amount);

        payment.setYandexPaymentId(node.get("id").asText());
        payment.setPaymentStatus(PaymentStatus.valueOf(node.get("status").asText().toUpperCase()));
        payment.setPaid(node.get("paid").asBoolean());

        try {
            String createdAt = node.get("created_at").asText();
            TimeZone tz = TimeZone.getTimeZone("UTC");
            DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            m_ISO8601Local.setTimeZone(tz);
            Date date = m_ISO8601Local.parse(createdAt);
            payment.setCreatedAt(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        payment.setDescription(node.get("description").asText());

        JsonNode paymentMethodNode = node.get("payment_method");
        if (paymentMethodNode != null) {
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setPaymentType(PaymentType.valueOf(paymentMethodNode.get("type").asText().toUpperCase())); //TODO MAY BE ERROR
            paymentMethod.setPaymentMethodId(paymentMethodNode.get("id").asText());
            paymentMethod.setSaved(paymentMethodNode.get("saved").asBoolean());
            payment.setPaymentMethod(paymentMethod);
        }

        JsonNode recipientNode = node.get("recipient");
        if (recipientNode != null) {
            Recipient recipient = new Recipient();
            recipient.setAccountId(recipientNode.get("account_id").asText());
            recipient.setGatewayId(recipientNode.get("gateway_id").asText());
            payment.setRecipient(recipient);
        }

        if (node.get("test") != null) {
            payment.setTest(node.get("test").asBoolean());
        }

        if (node.get("cancellation_details") != null) {
            JsonNode cancellationDetails = node.get("cancellation_details");
            PaymentCancelationDetails details = new PaymentCancelationDetails();
            details.setParty(cancellationDetails.get("party").asText());
            details.setReason(cancellationDetails.get("reason").asText());
            payment.setPaymentCancelationDetails(details);
        }

        if (node.get("refunded_amount") != null) {
            JsonNode refundedAmountNode = node.get("refunded_amount");
            Amount refundedAmount = new Amount();
            refundedAmount.setValue(refundedAmountNode.get("value").asDouble());
            refundedAmount.setCurrency(Currency.valueOf(refundedAmountNode.get("currency").asText().toUpperCase())); //TODO MAY BE ERROR
            payment.setRefundedAmount(refundedAmount);
        }
        return payment;
    }
}
