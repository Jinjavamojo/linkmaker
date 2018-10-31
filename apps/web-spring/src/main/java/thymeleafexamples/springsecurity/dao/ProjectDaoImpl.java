package thymeleafexamples.springsecurity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.Role;

import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Project findById(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Project.class, id);
    }

    @Override
    public List<Project> getUserProjects(String userName) {
        Session currentSession = sessionFactory.getCurrentSession();

        Query<Project> query = currentSession.createQuery("from Project as p where p.user.id = (select id from User where userName = :username)", Project.class);
        query.setParameter("username", userName);
        return query.list();
    }
}
