package thymeleafexamples.springsecurity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import thymeleafexamples.springsecurity.config.SessionAttr;

@Controller
@SessionAttributes("sessionAttr")
public class ReportController {

    @RequestMapping(value = "/download/txt",method = RequestMethod.GET)
    public ModelAndView showLinkedUsers(ModelAndView model, SessionAttr sessionAttr) {
        model.setViewName("404");
        return model;
    }
}
