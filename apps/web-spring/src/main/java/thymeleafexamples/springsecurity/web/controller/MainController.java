package thymeleafexamples.springsecurity.web.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.unbescape.html.HtmlEscape;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.service.ProjectService;
import thymeleafexamples.springsecurity.service.UserService;

/**
 * Application home page and login.
 */
@Controller
@SessionAttributes("sessionAttr")
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/")
    public String root(Locale locale) {
        return "redirect:/index.html";
    }

    @RequestMapping(value = "/project/new",method = RequestMethod.GET)
    public String showNew(Model model, HttpServletRequest request) {
        model.addAttribute("project", new Project());
        return "newProject";
    }

    @RequestMapping(value="/newsave", method = RequestMethod.POST, params={"save"})
    public ModelAndView saveNewProject(final @Valid Project project, final BindingResult bindingResult, final ModelMap model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            //return "projects";
        }
        Long save = projectService.save(project);
        // this.seedStarterService.add(seedStarter);
        model.clear();
        String contextPath = request.getServletContext().getContextPath();
        //model.put("baseUrl",baseUrl);
        //request.set
        //model.addAttribute("baseUrl", baseUrl);
        String baseUrl = String.format("%s://%s:%d%s",request.getScheme(),  request.getServerName(), request.getServerPort(),contextPath);

        //model.addAttribute("name", project.getName());
        //model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("saved", true);
//        "@{${'/projects/edit/' + project.id}}"
        return new ModelAndView("redirect:project/edit/" +  save, model);
        //return "redirect:/projects/edit/" + project.getId();
        //return "redirect:/index";
    }

    @RequestMapping(value = "/paidUsers",method = RequestMethod.GET)
    public String showPaidUsers(ModelAndView model, SessionAttr sessionAttr) {
        return "paidUsers";
    }

    @ModelAttribute
    public SessionAttr sessionAttr(){
        return new SessionAttr();
    }

    @RequestMapping(value = "/project/edit/{id}",method = RequestMethod.GET)
    public ModelAndView show(@PathVariable("id") Long id, @ModelAttribute("sessionAttr") SessionAttr sessionAttr, ModelAndView model, HttpServletRequest request) {
        String contextPath = request.getServletContext().getContextPath();
        String baseUrl = String.format("%s://%s:%d%s",request.getScheme(),  request.getServerName(), request.getServerPort(),contextPath);
        Project byId = projectService.findById(id);
        ModelMap m = new ModelMap();

        model.addObject("project", byId);
        //model.addObject("sessionAttr", new SessionAttr());
        sessionAttr.currentProjectId = id;
        model.setViewName("project");
        model.addObject("name", byId.getName());
        model.addObject("baseUrl", baseUrl);
        //model.addAttribute("saved",true);
        if (request.getParameter("saved") != null) {
            model.addObject("saved", request.getParameter("saved"));
        }
        return model;
    }

    @RequestMapping(value="/saveme", method = RequestMethod.POST, params={"save"})
    public ModelAndView saveProject(final @Valid Project project, final BindingResult bindingResult, final ModelMap model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("redirect:project/edit/" +  project.getId(), model); //TODO
        }
        boolean save = projectService.update(project);
        // this.seedStarterService.add(seedStarter);
        model.clear();
        String contextPath = request.getServletContext().getContextPath();
        //model.put("baseUrl",baseUrl);
        //request.set
        //model.addAttribute("baseUrl", baseUrl);
        String baseUrl = String.format("%s://%s:%d%s",request.getScheme(),  request.getServerName(), request.getServerPort(),contextPath);

        //model.addAttribute("name", project.getName());
        //model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("saved", true);
//        "@{${'/projects/edit/' + project.id}}"
        return new ModelAndView("redirect:project/edit/" +  project.getId(), model);
        //return "redirect:/projects/edit/" + project.getId();
        //return "redirect:/index";
    }

    @ModelAttribute("projects")
    public List<Project> listProjects() {
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
