package thymeleafexamples.springsecurity.web.controller;

import com.vk.api.sdk.actions.Account;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.OAuthException;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.account.Info;
import com.vk.api.sdk.objects.account.UserSettings;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.queries.account.AccountGetInfoQuery;
import com.vk.api.sdk.queries.account.AccountGetProfileInfoQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.service.PaymentService;
import thymeleafexamples.springsecurity.yandex.YandexKassaComponent;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.service.ProjectService;
import thymeleafexamples.springsecurity.service.VKService;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.REDIRECT_URI;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
@PropertySource({"classpath:app.properties"})
public class LinkGeneratorController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Environment env;

    @Autowired
    private VkApiClient vkApiClient;

    @Autowired
    private VKService vkService;

    @Autowired
    private YandexKassaComponent kassa;
    private String baseUrl;
    private String projectName;
    private String generateRedirectUri;
    //private String accessToken;

    private Logger logger = Logger.getLogger(getClass().getName());

    public ModelAndView saveVKUserAndRedirectToYandexPaymentLink(UserActor userActor, final ModelMap model, @Nullable String email) {
        try {

            //get vk user
            List<UserXtrCounters> getUsersResponse = vkApiClient.users().get(userActor).userIds(String.valueOf(userActor.getId())).execute();
            UserXtrCounters user = getUsersResponse.get(0);
            Long vkUserId = Long.valueOf(user.getId().toString());

            //save vk user
            VkUser vkUser = new VkUser();
            vkUser.setVkUserId(vkUserId);
            vkUser.setLastName(user.getLastName());
            vkUser.setFirstName(user.getFirstName());
            vkUser.setEmail(email);
            vkService.saveUserIfNotExists(vkUser);

            String clientMessage = String.format("Оплата курса: '%s', Пользователь: %s %s",projectName, user.getLastName(), user.getFirstName());
            Payment paymentFromRequest = kassa.createPaymentFromRequest("https://yandex.ru", clientMessage);

            if (paymentFromRequest != null) {
                paymentFromRequest.setVkUser(vkUser);
                paymentFromRequest.setProjectName(projectName);
                paymentService.savePayment(paymentFromRequest);
                clearSessionAttrs();
                return new ModelAndView("redirect:" + env.getProperty("yandexKassaPaymentURL") + paymentFromRequest.getYandexPaymentId());
            } else {
                model.addAttribute("info", "Error while redirecting");
                clearSessionAttrs();
                return new ModelAndView("payInfo", model);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "info section");
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
            clearSessionAttrs();
        }
        finally {
            clearSessionAttrs();
        }
        return new ModelAndView("redirect:" /*+ format*/);
    }

    @RequestMapping(value = "/get_code/*", method = RequestMethod.GET)
    public ModelAndView getCode(HttpServletRequest request, final ModelMap model) {
        try {
            try {
                UserAuthResponse authResponse = vkApiClient.oauth()
                        .userAuthorizationCodeFlow(Integer.valueOf(env.getProperty("clientId")), env.getProperty("clientSecret"), generateRedirectUri, request.getParameter("code"))
                        .execute();

                UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());
                String email = authResponse.getEmail();
                return saveVKUserAndRedirectToYandexPaymentLink(actor,model,email);
            } catch (OAuthException e) {
                e.getRedirectUri();
            }
            return new ModelAndView("redirect:" + "");

        } catch (Exception e) {

            //TODO USER OUPUT
            logger.log(Level.SEVERE, "getCode section");
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
            clearSessionAttrs();
        }
        //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        return new ModelAndView("redirect:" /*+ format*/); //todo error
    }

//    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
//    public ModelAndView method() {
//        return new ModelAndView("redirect:" + projectUrl);
//    }

    @RequestMapping(value = "/pay/*", method = RequestMethod.GET)
    public ModelAndView pay(HttpServletRequest request, HttpServletResponse response, final ModelMap model) {
        projectName = StringUtils.substringAfter(request.getRequestURL().toString(), "/pay/");
        if (StringUtils.isEmpty(projectName)) {
            model.addAttribute("info", "Empty project");
            return new ModelAndView("payInfo", model);
        }
        Project projectByName = projectService.getProjectByName(projectName);
        if (projectByName == null) {
            model.addAttribute("info", "Project doesn't exists");
            return new ModelAndView("payInfo", model);


        }




        baseUrl = StringUtils.substringBefore(request.getRequestURL().toString(), "/pay/");
        //generateRedirectUri="https://oauth.vk.com/blank.html";
        generateRedirectUri = generateRedirectUri(baseUrl, projectName);//s + "/get_code/" + projectName + "&scope=groups&response_type=code";
        //{"error":"invalid_scope","error_description":"standalone applications should use blank.html as redirect_uri to access messages"}
        String scope = "email,notify";
        String vkRedirectUrlStr = String.format("https://oauth.vk.com/authorize?client_id=%s&display=page&redirect_uri=%s&scope=%s&response_type=code", env.getProperty("clientId"), generateRedirectUri, scope);

//        try {
//            CloseableHttpClient client = HttpClients.createDefault();
//            HttpPost httpPost = new HttpPost(vkRedirectUrlStr);
//
//            String json = "{ id :1, name : John }";
//            StringEntity entity = new StringEntity(json);
//            httpPost.setEntity(entity);
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            CloseableHttpResponse closeablehttpresponse = client.execute(httpPost);
//            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(closeablehttpresponse.getEntity().getContent()))) {
//                String collect = buffer.lines().collect(Collectors.joining("\n"));
//                int g = 0;
//            }
//            //int g = 0;
//            //response.getEntity().getContent()
//            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
//            client.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //String format2 = String.format("https://api.vk.com/method/account.getProfileInfo&access_token=%s&v=5.92", accessToken);
        //ResponseEntity<String> response = restTemplate.getForEntity(format2, String.class);
        return new ModelAndView("redirect:" + vkRedirectUrlStr);

        //HttpGet request2 = new HttpGet(format);
        // add request header
        //request2.addHeader("User-Agent", USER_AGENT);
        //client.execute(request2);
//
//            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//
//            StringBuffer result = new StringBuffer();
//            String line = "";
//            while ((line = rd.readLine()) != null) {
//                result.append(line);
//            }
//            int g = 0;


        //second request
//            VkApiClient vk = new VkApiClient(new HttpTransportClient());
//            try {
//                UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), request2.getParameter("code")).execute();
//                response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
//            } catch (Exception e) {
//                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
//                response.getWriter().println("error");
//                response.setContentType("text/html;charset=utf-8");
//                e.printStackTrace();
//            }
        //return new ModelAndView("payInfo",model);
    }

    private void clearSessionAttrs() {
        baseUrl = null;
        projectName = null;
        generateRedirectUri = null;
    }

    private String generateRedirectUri(String baseRequestUri, String projectName) {
        return baseRequestUri + "/get_code/" + projectName;
    }
}
