package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.Project;

import java.util.List;

public interface ProjectDao {

    Project findById(Long id);

    Project getProjectByName(String name);

    List<Project> getUserProjects();

    List<Project> getUserProjectsWithAdditionalInfo();

    boolean update(Project project);

    Long save(Project project);
}
