package thymeleafexamples.springsecurity.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import thymeleafexamples.springsecurity.entity.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class ProjectController {

    @RequestMapping("/projects")
    public String projects() {
        return "/projects.html";
    }
//    @RequestMapping("/projects.html")
//    public String root(Locale locale) {
//        return "redirect:/projects.html";
//    }

    @ModelAttribute("projects")
    public List<Project> populateSeedStarters() {
        Project project = new Project();
        project.setName("123");
        List<Project> list = new ArrayList<>();
        list.add(project);
        return list;
    }
}
