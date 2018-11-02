package thymeleafexamples.springsecurity.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.service.ProjectService;

@Controller
@RequestMapping(value = "/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    //@RequestMapping("/projects")
    //public String projects() {
    //   return "/projects.html";
    //}
//    @RequestMapping("/projects.html")
//    public String root(Locale locale) {
//        return "redirect:/projects.html";
//    }

//    @ModelAttribute("projects")
//    public List<Project> populateSeedStarters() {
//        Project project = new Project();
//        project.setName("123");
//        List<Project> list = new ArrayList<>();
//        list.add(project);
//        return list;
//    }



//    @RequestMapping(value = "/edit/{id}",method = RequestMethod.POST)
//    public String show(@PathVariable("id") Long id, Model model) {
//        Project byId = projectService.findById(id);
//        model.addAttribute("project", byId);
//        model.addAttribute("name", byId.getName());
//        return "projects";
//    }
}
