package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.entity.Project;

import java.util.List;

public interface ProjectDao {

    Project findById(Long id);

    List<Project> getUserProjects(String userName);
}
