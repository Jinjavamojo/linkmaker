package thymeleafexamples.springsecurity.web.controller;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.unbescape.html.HtmlEscape;
import thymeleafexamples.springsecurity.config.SessionAttr;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;
import thymeleafexamples.springsecurity.service.ProjectService;
import thymeleafexamples.springsecurity.service.UserService;
import thymeleafexamples.springsecurity.service.VKService;

/**
 * Application home page and login.
 */
@Controller
@SessionAttributes("sessionAttr")
public class MainController {


    @Autowired
    private ServletContext servletContext; //TODO WHAT IS THIS?

    @Autowired
    private VKService vkService;

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

    //@RequestMapping(value = "/project/statistic/paid/report/txt",method = RequestMethod.GET)
    //@RequestMapping(value = "/project/statistic/paid/report/xlsx",method = RequestMethod.GET)


    //StreamingResponseBodyExample
    @RequestMapping(value = "/project/statistic/paid/report/txt",method = RequestMethod.GET)
    public void report(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpServletResponse response, HttpServletRequest request) {
        MediaType mediaType = MediaType.TEXT_PLAIN;//MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        //System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + MediaType.TEXT_PLAIN);
        try {

            //List<VkUserPaymentDTO> paidUsers = vkService.getPaidUsers();
            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);
            OutputStreamWriter       outputwriter       = new OutputStreamWriter(bos);
            outputwriter.write("rfefefe\n");
            outputwriter.write("2rfefefe2\n");

            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" +"text.txt");
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            //response.setContentType("application/download");
            response.setContentType(mediaType1.toString());
            outputwriter.flush();
            outputwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //JUST FOR TEST AJAX
    /*@RequestMapping(value = "/project/statistic/report",method = RequestMethod.POST)
    public void report(ModelAndView model, HttpServletResponse response, HttpServletRequest request) {
        MediaType mediaType = MediaType.TEXT_PLAIN;//MediaTypeUtils.getMediaTypeForFileName(this.servletContext, fileName);
        //System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + MediaType.TEXT_PLAIN);
        try {
            OutputStream fout= response.getOutputStream();
            OutputStream bos = new BufferedOutputStream(fout);
            OutputStreamWriter       outputwriter       = new OutputStreamWriter(bos);
            outputwriter.write("rfefefe\n");
            outputwriter.write("2rfefefe2\n");

            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" +"text.txt");
            MediaType mediaType1 = MediaType.parseMediaType("application/octet-stream");
            //response.setContentType("application/download");
            response.setContentType(mediaType1.toString());
            outputwriter.flush();
            outputwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    @RequestMapping(value="/newsave", method = RequestMethod.POST, params={"save"})
    public String saveNewProject(final @Valid Project project, final BindingResult bindingResult, final ModelMap model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "newProject";
            //new ModelAndView("/newsave" , model);
        }
        Long save = projectService.save(project);
        // this.seedStarterService.add(seedStarter);
        model.clear();
        String contextPath = request.getServletContext().getContextPath();
        //model.put("baseUrl",baseUrl);
        //request.set
        //model.addAttribute("baseUrl", baseUrl);
        String baseUrl = String.format("%s://%s:%d%s",request.getScheme(),  request.getServerName(), request.getServerPort(),contextPath);

        //model.addAttribute("nam98/e", project.getName());
        //model.addAttribute("baseUrl", baseUrl);
        //model.addAttribute("saved", true);
//        "@{${'/projects/edit/' + project.id}}"
        //return new ModelAndView("redirect:project/edit/" +  save, model);
        return "redirect:/project/edit/" + project.getId();
        //return "redirect:/index";
    }



    @ModelAttribute
    public SessionAttr sessionAttr(){
        return new SessionAttr();
    }

    @RequestMapping(value = "/project/edit/",method = RequestMethod.GET)
    public ModelAndView getProjectWithoutId(@ModelAttribute("sessionAttr") SessionAttr sessionAttr, HttpSession httpSession, ModelAndView model, HttpServletRequest request){
        return getProjectById(sessionAttr.currentProjectId,httpSession,sessionAttr,model,request);
    }

    @RequestMapping(value = "/project/edit/{id}",method = RequestMethod.GET)
    public ModelAndView getProjectById(@PathVariable("id") Long id, HttpSession httpSession, @ModelAttribute("sessionAttr") SessionAttr sessionAttr, ModelAndView model, HttpServletRequest request) {
        String contextPath = request.getServletContext().getContextPath();
        String baseUrl = String.format("%s://%s:%d%s",request.getScheme(),  request.getServerName(), request.getServerPort(),contextPath);
        Project byId = projectService.findById(id);
        if (byId == null) {
            model.setViewName("404");
            return model;
        }
        if (httpSession.getAttribute("userId") == null || byId.getUser().getId() != httpSession.getAttribute("userId")) {
            model.setViewName("404");
            return model;
        }

        model.addObject("project", byId);
        model.addObject("activeTab","main_info");
        //model.addObject("sessionAttr", sessionAttr);
        sessionAttr.currentProjectId = id;
        model.setViewName("project/project");
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

    @ModelAttribute("activeTab")
    public String activeTab(HttpServletRequest request) {
        String activeTab = (String) request.getSession().getAttribute("activeTab");
        return StringUtils.isEmpty(activeTab) ? "main_info" : activeTab;
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
