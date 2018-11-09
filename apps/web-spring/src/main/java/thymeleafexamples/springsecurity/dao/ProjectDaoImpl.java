package thymeleafexamples.springsecurity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.Role;
import thymeleafexamples.springsecurity.entity.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private HttpSession httpSession;

    @Override
    public Project findById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Project.class, id);
    }

    @Override
    public Project getProjectByName(String name) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Project> query = currentSession.createQuery("from Project as p where p.name = :projectname", Project.class);
        query.setParameter("projectname", name);
        List<Project> list = query.list();
        if (list.size() > 1) {
            throw new RuntimeException(String.format("Project with name = %s is more than 1",name));
        }
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Project> getUserProjects() {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User)httpSession.getAttribute("user");
        if (user == null) {
            return new ArrayList<>();
        }
        Query<Project> query = currentSession.createQuery("from Project as p where p.user.id = :user", Project.class);
        query.setParameter("user", user.getId());
        //Query<Project> query = currentSession.createQuery("from Project as p where p.user.id = (select id from User where userName = :username)", Project.class);
        return query.list();
    }

    @Override
    public boolean update(Project project) {
        Session currentSession = sessionFactory.getCurrentSession();
        Project newProject = currentSession.get(Project.class, project.getId());
        newProject.setName(project.getName());
        newProject.setProjectDescription(project.getProjectDescription());
        newProject.setProjectStartDate(project.getProjectStartDate());
        newProject.setAutoPaymentAvailable(project.getAutoPaymentAvailable());
        currentSession.update(newProject);
        return true;
    }

    @Override
    public Long save(Project project) {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User)httpSession.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("No session");
        }
        project.setUser(user);
        currentSession.save(project);
        return project.getId();
    }
}
