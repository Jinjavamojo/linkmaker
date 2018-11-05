package thymeleafexamples.springsecurity.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.unbescape.html.HtmlEscape;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.User;
import thymeleafexamples.springsecurity.service.ProjectService;
import thymeleafexamples.springsecurity.service.UserService;

/**
 * Application home page and login.
 */
@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/projects/edit/{id}",method = RequestMethod.GET)
    public String show(@PathVariable("id") Long id, Model model) {
        Project byId = projectService.findById(id);
        model.addAttribute("project", byId);
        model.addAttribute("name", byId.getName());
        return "projects";
    }

    @RequestMapping(value="/saveme", method = RequestMethod.POST, params={"save"})
    public String saveProject(final @Valid Project project, final BindingResult bindingResult, final ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "projects";
        }
        boolean save = projectService.save(project);
        // this.seedStarterService.add(seedStarter);
        model.clear();
//        "@{${'/projects/edit/' + project.id}}"
        return "redirect:/projects/edit/" + project.getId();
        //return "redirect:/index";
    }

    @ModelAttribute("projects")
    public List<Project> populateSeedStarters() {
//        if (SecurityContextHolder.getContext().getAuthentication() == null) {
//            return new ArrayList<>();
//        }
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal == null || (principal instanceof String && principal.toString().equals("anonymousUser"))) {
//            return new ArrayList<>();
//        }
//        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        List<Project> userProjects = projectService.getUserProjects();
        return userProjects;
    }

    @RequestMapping("/index.html")
    public String index() {
        return "index";
    }

    @RequestMapping("/login.html")
    public String login() {
        return "login";
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping("/simulateError.html")
    public void simulateError() {
        throw new RuntimeException("This is a simulated error message");
    }

    @RequestMapping("/error.html")
    public String error(HttpServletRequest request, Model model) {
        model.addAttribute("errorCode", "Error " + request.getAttribute("javax.servlet.error.status_code"));
        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("<ul>");
        while (throwable != null) {
            errorMessage.append("<li>").append(HtmlEscape.escapeHtml5(throwable.getMessage())).append("</li>");
            throwable = throwable.getCause();
        }
        errorMessage.append("</ul>");
        model.addAttribute("errorMessage", errorMessage.toString());
        return "error";
    }

    @RequestMapping("/403.html")
    public String forbidden() {
        return "403";
    }


}
