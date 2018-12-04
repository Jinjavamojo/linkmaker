package thymeleafexamples.springsecurity.yandex;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@PropertySource({"classpath:app.properties"})
public class YandexKassaComponent {

    @Autowired
    private Environment env;

    @Autowired
    private CredentialsProvider yandexCredentialsProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ObjectMapper paymentDeserializersStatus;

    @Autowired
    private String yandexCreatePaymentTemplate;

    private Logger logger = Logger.getLogger(getClass().getName());

    public Payment generateGetPaymentInfo(String paymentId) {


        try {
            HttpGet post = new HttpGet(env.getProperty("yandexKassaURL") + "/" + paymentId);
            HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(yandexCredentialsProvider).build();
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            if (HttpServletResponse.SC_OK == statusCode) {
                Payment readValue = paymentDeserializersStatus.readValue(result.toString(), Payment.class);
                return readValue;

            } else {
                logger.log(Level.SEVERE, String.valueOf(statusCode));
                logger.log(Level.SEVERE, EntityUtils.toString(response.getEntity()));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
        }

        try {
            HttpGet post = new HttpGet(env.getProperty("yandexKassaURL") + "/" + paymentId);
            HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(yandexCredentialsProvider).build();
            HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            // Print out the response message
            System.out.println(EntityUtils.toString(response.getEntity()));
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            if (HttpServletResponse.SC_OK == statusCode) {
                Payment readValue = paymentDeserializersStatus.readValue(result.toString(), Payment.class);
                return readValue;

            } else {
                logger.log(Level.SEVERE, EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
        }
        return null;
    }

    public Payment createPaymentFromRequest(String redirectLink, String paymentName) throws Exception {
        HttpPost post = new HttpPost(env.getProperty("yandexKassaURL"));
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(yandexCredentialsProvider).build();

        //String json = Utils.readLineByLineJava8("base_request.json");
        String replace = StringUtils.replace(yandexCreatePaymentTemplate, "${redirect_link}", redirectLink);
        String replace1 = StringUtils.replace(replace, "${description}", paymentName);

        HttpEntity entity = new ByteArrayEntity(replace1.getBytes("UTF-8"));
        post.setEntity(entity);

        post.addHeader("Content-type", "application/json");
        post.addHeader("Idempotence-Key", UUID.randomUUID().toString());
        HttpResponse response = client.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();

            try {
                // Print out the response message
                //System.out.println(EntityUtils.toString(response.getEntity()));
                BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                if (HttpServletResponse.SC_OK == statusCode) {
                    Payment readValue = objectMapper.readValue(result.toString(), Payment.class);
                    logger.log(Level.INFO,String.format("Payment %s is created", readValue.getYandexPaymentId()));
                    return readValue;

                } else {
                    logger.log(Level.SEVERE, result.toString());
                }
                return null;
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.toString());
            }

        return null;
    }
}
