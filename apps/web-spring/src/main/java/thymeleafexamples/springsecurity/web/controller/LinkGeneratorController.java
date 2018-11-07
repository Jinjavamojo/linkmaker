package thymeleafexamples.springsecurity.web.controller;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.component.YandexKassa;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.service.ProjectService;
import thymeleafexamples.springsecurity.service.VKService;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

@Controller
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PropertySource({"classpath:app.properties"})
public class LinkGeneratorController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private Environment env;

    @Autowired
    private VkApiClient vkApiClient;

    @Autowired
    private VKService vkService;

    @Autowired
    private YandexKassa kassa;

    @RequestMapping(value = "/info/*", method = RequestMethod.GET)
    public ModelAndView getInfo(HttpServletRequest request, final ModelMap model) {
        try {
            UserActor actor = new UserActor(Integer.parseInt(request.getParameter("user")), request.getParameter("token"));
            List<UserXtrCounters> getUsersResponse = vkApiClient.users().get(actor).userIds(request.getParameter("user")).execute();
            UserXtrCounters user = getUsersResponse.get(0);

            Long vkUserId = Long.valueOf(user.getId().toString());
            VkUser vkUser = new VkUser();
            vkUser.setVkUserId(vkUserId);
            vkUser.setLastname(user.getLastName());
            vkUser.setFirstName(user.getFirstName());

            vkService.saveUserIfNotExists(vkUser);

            Payment paymentFromRequest = kassa.createPaymentFromRequest("https://yandex.ru", "payment1/" + vkUserId);
            int g = 0;
            //response.setContentType("text/html;charset=utf-8");
            //response.setStatus(HttpServletResponse.SC_OK);
            //response.getWriter().println(getInfoPage(user));

        } catch (Exception e) {
            //response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            //response.getWriter().println("error");
            //response.setContentType("text/html;charset=utf-8");
            e.printStackTrace();
        }
        //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        return new ModelAndView("redirect:" /*+ format*/);
    }

    @RequestMapping(value = "/get_code/*", method = RequestMethod.GET)
    public ModelAndView getCode(HttpServletRequest request, final ModelMap model) {
        try {
            String baseUrl = StringUtils.substringBefore(request.getRequestURL().toString(), "/get_code/");
            String project = StringUtils.substringAfter(request.getRequestURL().toString(), "/get_code/");
            //baseUrl = ""
            String redirectUri = generateRedirectUri(baseUrl, project);
            UserAuthResponse authResponse = vkApiClient.oauth().userAuthorizationCodeFlow(Integer.valueOf(env.getProperty("clientId")), env.getProperty("clientSecret"), redirectUri, request.getParameter("code")).execute();
            String s = baseUrl + "/info/" + project + "?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId();
            return new ModelAndView("redirect:" + s);
            //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
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
        String projectName = StringUtils.substringAfter(request.getRequestURL().toString(), "/pay/");
        if (StringUtils.isEmpty(projectName)) {
            model.addAttribute("info", "Empty project");
            return new ModelAndView("payInfo", model);
        }
        Project projectByName = projectService.getProjectByName(projectName);
        if (projectByName == null) {
            model.addAttribute("info", "Project doesn't exists");
            return new ModelAndView("payInfo", model);


        }

        String s = StringUtils.substringBefore(request.getRequestURL().toString(), "/pay/");
        String redirectLink = generateRedirectUri(s, projectName);//s + "/get_code/" + projectName + "&scope=groups&response_type=code";
        String vkRedirectUrlStr = String.format("https://oauth.vk.com/authorize?client_id=%sdisplay=page&redirect_uri=%s&scope=groups&response_type=code", env.getProperty("clientId"), redirectLink);
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

    private String generateRedirectUri(String baseRequestUri, String projectName) {
        return baseRequestUri + "/get_code/" + projectName;
    }
}
