package thymeleafexamples.springsecurity.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.User;

import java.util.List;

public interface ProjectService {

    Project findById(Long id);

    List<Project> getUserProjects();

    boolean save(Project project);

    Project getProjectByName(String name);
}
