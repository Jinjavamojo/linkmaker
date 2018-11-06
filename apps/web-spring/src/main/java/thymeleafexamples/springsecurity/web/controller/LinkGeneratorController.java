package thymeleafexamples.springsecurity.web.controller;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

@Controller
public class LinkGeneratorController {

    @Autowired
    private ProjectService projectService;

    private int clientId = 6718322;
    private String clientSecret = "wZC2zZHL3N580a77CE3D";
    private String SS = "https://oauth.vk.com/authorize?client_id=6718322&display=page&redirect_uri=http://localhost:8090/callback&scope=groups&response_type=code";

    @RequestMapping(value = "/get_code/*", method = RequestMethod.GET)
    public ModelAndView getCode(HttpServletRequest request, final ModelMap model) {
        VkApiClient vk = new VkApiClient(new HttpTransportClient());
        try {
            UserAuthResponse authResponse = vk.oauth().userAuthorizationCodeFlow(clientId, clientSecret, getRedirectUri(), request.getParameter("code")).execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        //response.sendRedirect("/info?token=" + authResponse.getAccessToken() + "&user=" + authResponse.getUserId());
        return new ModelAndView("redirect:" /*+ format*/);
    }

//    @RequestMapping(value = "/redirect", method = RequestMethod.GET)
//    public ModelAndView method() {
//        return new ModelAndView("redirect:" + projectUrl);
//    }

    @RequestMapping(value = "/pay/*", method = RequestMethod.GET)
    public ModelAndView pay(HttpServletRequest request,HttpServletResponse response, final ModelMap model) {
        String projectName = StringUtils.substringAfter(request.getRequestURL().toString(), "/pay/");
        if (StringUtils.isEmpty(projectName)) {
            model.addAttribute("info", "Empty project");
            return new ModelAndView("payInfo",model);
        }
        Project projectByName = projectService.getProjectByName(projectName);
        if (projectByName == null) {
            model.addAttribute("info", "Project doesn't exists");
            return new ModelAndView("payInfo",model);


        }

        //HttpPost post = new HttpPost(SS);

       // HttpClient client = HttpClientBuilder.create().build();
        String s = StringUtils.substringBefore(request.getRequestURL().toString(), "/pay/");
        String redirectLink = s + "/get_code/" + projectName + "&scope=groups&response_type=code";
            HttpClient client = HttpClientBuilder.create().build();

            String format = String.format("https://oauth.vk.com/authorize?client_id=%sdisplay=page&redirect_uri=%s", clientId, redirectLink);
            //response.sendRedirect(format);
            return new ModelAndView("redirect:" + format);

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

    private String getRedirectUri() {
        return "http://localhost:9090/web-spring/callback";
    }
}
