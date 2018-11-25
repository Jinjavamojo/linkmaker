package thymeleafexamples.springsecurity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import thymeleafexamples.springsecurity.yandex.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentDeserializerStatus extends StdDeserializer<Payment> {

    private Logger logger = Logger.getLogger(getClass().getName());

    public PaymentDeserializerStatus() {
        this(null);
    }

    public PaymentDeserializerStatus(Class<?> vc) {
        super(vc);
    }

    @Override
    public Payment deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        Payment payment = new Payment();
        payment.setYandexPaymentId(node.get("id").asText());
        payment.setPaymentStatus(PaymentStatus.valueOf(node.get("status").asText().toUpperCase()));
        try {
            if (node.get("captured_at") != null) {
                String createdAt = node.get("captured_at").asText();
                TimeZone tz = TimeZone.getTimeZone("UTC");
                DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                m_ISO8601Local.setTimeZone(tz);
                Date date = m_ISO8601Local.parse(createdAt);
                payment.setCapturedAt(date);
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, e.getMessage());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
        }
        return payment;
    }
}
