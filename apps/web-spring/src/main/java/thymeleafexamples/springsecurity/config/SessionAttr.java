package thymeleafexamples.springsecurity.config;

import org.springframework.stereotype.Component;

@Component
public class SessionAttr {

    public Long currentProjectId;

    public String projectName;

    public String generateRedirectUri;
    public String baseUrl;

    public void clear() {
        projectName = null;
        generateRedirectUri = null;
        baseUrl = null;
    }

}
