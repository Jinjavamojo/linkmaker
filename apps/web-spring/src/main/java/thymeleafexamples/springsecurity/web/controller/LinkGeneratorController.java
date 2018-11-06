package thymeleafexamples.springsecurity.web.controller;

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
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.service.ProjectService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.apache.http.HttpHeaders.USER_AGENT;

@Controller
public class LinkGeneratorController {

    @Autowired
    private ProjectService projectService;

    private String clientId = "6718322";
    private String SS = "https://oauth.vk.com/authorize?client_id=6718322&display=page&redirect_uri=http://localhost:8090/callback&scope=groups&response_type=code";

    @RequestMapping(value = "/get_code/*", method = RequestMethod.GET)
    public String getCode(HttpServletRequest request, final ModelMap model) {

        return null;
    }

    @RequestMapping(value = "/pay/*", method = RequestMethod.GET)
    public String pay(HttpServletRequest request, final ModelMap model) {
        String projectName = StringUtils.substringAfter(request.getRequestURL().toString(), "/pay/");
        if (StringUtils.isEmpty(projectName)) {
            model.addAttribute("info", "Empty project");
            return "payInfo";
        }
        Project projectByName = projectService.getProjectByName(projectName);
        if (projectByName == null) {
            model.addAttribute("info", "Project doesn't exists");
            return "payInfo";
        }

        //HttpPost post = new HttpPost(SS);

       // HttpClient client = HttpClientBuilder.create().build();

        try {
            HttpClient client = HttpClientBuilder.create().build();
            String redirectLink = request.getLocalName() + "/get_code/" + projectName + "&scope=groups&response_type=code";
            String format = String.format("https://oauth.vk.com/authorize?client_id=%sdisplay=page&redirect_uri=%s", clientId, redirectLink);
            HttpGet request2 = new HttpGet(format);

            // add request header
            request2.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request2);

            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));

            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            int g = 0;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "pay";
    }
}
