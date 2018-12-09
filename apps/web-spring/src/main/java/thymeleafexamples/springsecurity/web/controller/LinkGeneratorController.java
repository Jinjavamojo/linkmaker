package thymeleafexamples.springsecurity.web.controller;

import com.vk.api.sdk.actions.Account;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
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

    @RequestMapping(value = "/info/*", method = RequestMethod.GET)
    public ModelAndView getInfo(HttpServletRequest request, final ModelMap model) {
        try {
            UserActor actor = new UserActor(Integer.parseInt(request.getParameter("user")), request.getParameter("token"));
            List<UserXtrCounters> getUsersResponse = vkApiClient.users().get(actor).userIds(request.getParameter("user")).execute();
            UserXtrCounters user = getUsersResponse.get(0);

            Account account = vkApiClient.account();
            AccountGetInfoQuery info = account.getInfo(actor);
            //Info execute = info.execute();

            Long vkUserId = Long.valueOf(user.getId().toString());
            VkUser vkUser = new VkUser();
            vkUser.setVkUserId(vkUserId);
            vkUser.setLastName(user.getLastName());
            vkUser.setFirstName(user.getFirstName());

            Account account2 = vkApiClient.account();
            AccountGetProfileInfoQuery profileInfo = account2.getProfileInfo(actor);
            //UserSettings execute2 = profileInfo.execute();

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

            //response.setContentType("text/html;charset=utf-8");
            //response.setStatus(HttpServletResponse.SC_OK);
            //response.getWriter().println(getInfoPage(user));

        } catch (Exception e) {
            //response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            //response.getWriter().println("error");
            //response.setContentType("text/html;charset=utf-8");
            logger.log(Level.SEVERE, "info section");
            logger.log(Level.SEVERE, e.toString());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
            clearSessionAttrs();

        }
        finally {
            clearSessionAttrs();
        }
        //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        return new ModelAndView("redirect:" /*+ format*/);
    }

    @RequestMapping(value = "/get_code/*", method = RequestMethod.GET)
    public ModelAndView getCode(HttpServletRequest request, final ModelMap model) {
        try {
            //String baseUrl = StringUtils.substringBefore(request.getRequestURL().toString(), "/get_code/");
            //String project = StringUtils.substringAfter(request.getRequestURL().toString(), "/get_code/");
            //baseUrl = ""
            //String redirectUri = generateRedirectUri(baseUrl, project);
            UserAuthResponse authResponse = vkApiClient.oauth().userAuthorizationCodeFlow(Integer.valueOf(env.getProperty("clientId")), env.getProperty("clientSecret"), generateRedirectUri, request.getParameter("code")).execute();
            String token = authResponse.getAccessToken();
            String s = baseUrl + "/info/" + projectName + "?token=" + token + "&user=" + authResponse.getUserId();// + "&accessToken=" + env.getProperty("accessToken");
            RestTemplate restTemplate = new RestTemplate();

            //String format2 = String.format("https://api.vk.com/method/account.getProfileInfo&access_token=%s&v=5.92", accessToken);
            //ResponseEntity<String> response = restTemplate.getForEntity(format2, String.class);
            return new ModelAndView("redirect:" + s);
            //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());

        } catch (Exception e) {
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
        generateRedirectUri = generateRedirectUri(baseUrl, projectName);//s + "/get_code/" + projectName + "&scope=groups&response_type=code";
        //{"error":"invalid_scope","error_description":"standalone applications should use blank.html as redirect_uri to access messages"}
        String scope = "email,notify";
        String vkRedirectUrlStr = String.format("https://oauth.vk.com/authorize?client_id=%sdisplay=page&redirect_uri=%s&scope=%s&response_type=code", env.getProperty("clientId"), generateRedirectUri, scope);
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
