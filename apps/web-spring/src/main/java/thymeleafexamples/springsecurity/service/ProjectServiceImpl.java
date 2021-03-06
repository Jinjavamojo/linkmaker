package thymeleafexamples.springsecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.dao.ProjectDao;
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
    public List<Project> getUserProjects() {
        return projectDao.getUserProjects();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getUserProjectsWithAdditionalInfo() {
        return projectDao.getUserProjectsWithAdditionalInfo();
    }

    @Override
    @Transactional
    public boolean update(Project project) {
        return projectDao.update(project);

    }

    @Override
    @Transactional
    public Long save(Project project) {
        return projectDao.save(project);

    }

    @Override
    @Transactional(readOnly = true)
    public Project getProjectByName(String name) {
        return projectDao.getProjectByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public double getProjectMoneySum(Long projectId) {
        return projectDao.getProjectMoneySum(projectId);
    }


    @Override
    @Transactional(readOnly = true)
    public int getUniqPaidUsers(Long projectId) {
        return projectDao.getUniqPaidUsers(projectId);
    }


    @Override
    @Transactional(readOnly = true)
    public int getUniqueVisitedUsersOfProject(Long projectId) {
        return projectDao.getUniqueVisitedUsersOfProject(projectId);
    }

    //void save(CrmUser crmUser);

    @Override
    @Transactional(readOnly = true)
    public double getTotalMoneyOfAllProjects() {
        return projectDao.getTotalMoneyOfAllProjects();
    }

    @Override
    @Transactional(readOnly = true)
    public int getUserProjectsCount() {
        return projectDao.getUserProjectsCount();
    }
}
