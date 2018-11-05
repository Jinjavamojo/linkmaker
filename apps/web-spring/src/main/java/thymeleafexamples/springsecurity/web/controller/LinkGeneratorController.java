package thymeleafexamples.springsecurity.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LinkGeneratorController {

    @RequestMapping(value = "/pay/*", method = RequestMethod.GET)
    public String pay(HttpServletRequest request) {
        String projectName = StringUtils.substringAfter(request.getRequestURL().toString(), "/pay/");
        return "pay";
    }
}
