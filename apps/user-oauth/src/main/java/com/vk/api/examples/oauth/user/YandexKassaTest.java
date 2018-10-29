package com.vk.api.examples.oauth.user;

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
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thymeleafexamples.springsecurity.yandex.Payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

public class YandexKassaTest {

    private static final Logger LOG = LoggerFactory.getLogger(YandexKassaTest.class);

    private static final String YANDEX_KASSA_API = "https://payment.yandex.net/api/v3/payments";
    private static final String SHOP_ID = "545016";
    private static final String PASS = "test_M7ykGn7_OvItR9QlxjBhrzaGBQvOeijsJ5YCWD8ClcU";

    public static void main(String[] args) throws Exception {

        //generateGetPaymentInfo("2362e42c-000f-5000-8000-151677e0ff24");
        String requestToGeneratePayment = createRequestToGeneratePayment();

        ObjectMapper objectMapper = new ObjectMapper();

        Payment payment = objectMapper.readValue(requestToGeneratePayment, Payment.class);
        int g = 0;

    }

    private static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(Integer.toString(b, 16));
        }
        return builder.toString();
    }
    private static String generateUUID4() {
        String digest;
        try {
            //MessageDigest salt = MessageDigest.getInstance("SHA-256");
            //salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
            //digest = bytesToHex(UUID.randomUUID().toString().getBytes("UTF-8"));
            //TODO: CHECK UUUD4 VERSION
            return UUID.randomUUID().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //return digest;
    }

    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    private static CredentialsProvider generateCredentials(String shopId, String pass) {
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(shopId, pass);
        provider.setCredentials(AuthScope.ANY, credentials);
        return provider;
    }

    private static String generateGetPaymentInfo(String paymentId) throws Exception {
        HttpGet post = new HttpGet(YANDEX_KASSA_API + "/" + paymentId);
        CredentialsProvider credentials = generateCredentials(SHOP_ID, PASS);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentials).build();

        HttpResponse response = client.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        try {
            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject jsonObj = new JSONObject(result.toString());
            return jsonObj.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String createRequestToGeneratePayment() throws Exception {
        HttpPost post = new HttpPost(YANDEX_KASSA_API);

        CredentialsProvider credentials = generateCredentials(SHOP_ID, PASS);
        HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentials).build();

        String json = readLineByLineJava8("base_request.json");
        String replace = StringUtils.replace(json, "${redirect_link}", "https://yandex.ru");
        String replace1 = StringUtils.replace(replace, "${description}", "Платеж №1");

        HttpEntity entity = new ByteArrayEntity(replace1.getBytes("UTF-8"));
        post.setEntity(entity);

        post.addHeader("Content-type", "application/json");
        post.addHeader("Idempotence-Key", generateUUID4());
        HttpResponse response = client.execute(post);

        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        try {
            // Print out the response message
            //System.out.println(EntityUtils.toString(response.getEntity()));
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            //JSONObject jsonObj = new JSONObject(result.toString());
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
