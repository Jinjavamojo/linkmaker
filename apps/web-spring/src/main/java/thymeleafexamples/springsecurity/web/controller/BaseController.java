package thymeleafexamples.springsecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import thymeleafexamples.springsecurity.Utils;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.service.ProjectService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BaseController {


    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    protected ProjectService projectService;

    @ModelAttribute("totalMoney")
    public String totalMoney() {
        double totalMoneyOfAllProjects = projectService.getTotalMoneyOfAllProjects();
        return Utils.df.format(totalMoneyOfAllProjects);
    }

    @ModelAttribute("projectsCount")
    public String projectsCount() {
        return String.valueOf(projectService.getUserProjectsCount());
    }

    @ModelAttribute("projects")
    public List<Project> listProjects() {
        try {
            return projectService.getUserProjectsWithAdditionalInfo();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            logger.log(Level.SEVERE, Utils.getStackTrace(e));
        }
        throw new RuntimeException("some error raise above");
    }
}
