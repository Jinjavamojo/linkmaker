package thymeleafexamples.springsecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.dao.ProjectDao;
import thymeleafexamples.springsecurity.dao.UserDao;
import thymeleafexamples.springsecurity.entity.Project;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Override
    @Transactional(readOnly = true)
    public Project findById(Long id) {
        return projectDao.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getUserProjects(String userName) {
        return projectDao.getUserProjects(userName);
    }

    //void save(CrmUser crmUser);
}
