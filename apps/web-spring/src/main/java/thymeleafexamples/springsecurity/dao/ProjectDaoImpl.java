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
    public boolean save(Project project) {
        Session currentSession = sessionFactory.getCurrentSession();
        Project newProject = currentSession.get(Project.class, project.getId());
        newProject.setName(project.getName());
        currentSession.update(newProject);
        //currentSession.update(project1);
        return true;
        //Project new = currentSession.get(Project.class, project.getId());



    }
}
