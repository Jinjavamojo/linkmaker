package thymeleafexamples.springsecurity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class DownloadController {

    @RequestMapping(value="/download/paidUser/txt", method = RequestMethod.GET)
    public void downloadFile(HttpSession httpSession, HttpServletResponse response, @PathVariable("type") String type) {

    }
}
