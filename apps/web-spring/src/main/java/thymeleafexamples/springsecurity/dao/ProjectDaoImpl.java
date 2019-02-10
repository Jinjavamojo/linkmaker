package thymeleafexamples.springsecurity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.Role;
import thymeleafexamples.springsecurity.entity.User;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ProjectDaoImpl implements ProjectDao {

    //Оплатившие, уникально + сумма
    private String queryPaymentCountAndSum =
            "select count(distinct(vk_user)),sum(pa.value) from payments pa join projects pr on pa.project = pr.id  where pr.user_id = :userId AND payment_status = 'SUCCEEDED'  and pr.id = :projectId ";

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
    public int getUserProjectsCount() {
        Session currentSession = sessionFactory.getCurrentSession();
        User user = (User)httpSession.getAttribute("user");
        if (user == null) {
            return 0;
        }
        return ((BigInteger) sessionFactory.getCurrentSession().createNativeQuery("SELECT COUNT(*) FROM PROJECTS WHERE user_id=:userId")
                .setParameter("userId", user.getId()).uniqueResult()).intValue();
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


    //Берем все проекты у пользователя. У каждого проекта высчитываем
    //кол-во уникальных переходов, оплативших пользователей и сумму денег по проекту.
    @Override
    public List<Project> getUserProjectsWithAdditionalInfo() {

        User user = (User)httpSession.getAttribute("user");
        List<Project> userProjects = getUserProjects();

        for (Project userProject : userProjects) {
            Integer uniquePaidCount = 0;
            Double projectMoneySum = 0d;

            int uniqClick = getUniqClick(userProject.getId());

            List<Object[]> tuples2 = sessionFactory.getCurrentSession().createNativeQuery(
                    queryPaymentCountAndSum)
                    .setParameter("projectId", userProject.getId())
                    .setParameter("userId", user.getId())
                    .list();
            Object[] objects = tuples2.get(0);
            uniquePaidCount = ((BigInteger)objects[0]).intValue();
            projectMoneySum = (Double)objects[1];

            userProject.setUniqueClicksCount(uniqClick);
            userProject.setPaidCount(uniquePaidCount);
            if (projectMoneySum != null) {
                userProject.setTotalMoneyOfProject(projectMoneySum);
            }
        }
        return userProjects;
    }

    @Override
    public int getUniqClick(Long projectId) {
        User user = (User)httpSession.getAttribute("user");
        //уникальные переходы( оплатившие входят сюда)
        String queryUniqueClick =
                "select count(distinct(vk_user)) from payments pa join projects pr on pa.project = pr.id  where pr.user_id = :userId and pr.id = :projectId ";
        return ((BigInteger)sessionFactory.getCurrentSession().createNativeQuery(
                queryUniqueClick)
                .setParameter("projectId", projectId)
                .setParameter("userId", user.getId())
                .uniqueResult()).intValue();
    }

    @Override
    public double getProjectMoneySum(Long projectId) {
        User user = (User)httpSession.getAttribute("user");
        List<Object[]> tuples2 = sessionFactory.getCurrentSession().createNativeQuery(
                queryPaymentCountAndSum)
                .setParameter("projectId", projectId)
                .setParameter("userId", user.getId())
                .list();
        Object[] objects = tuples2.get(0);
        if (objects[1] == null)
            return 0;
        return (Double)objects[1];
    }

    @Override
    public int getUniqPaidUsers(Long projectId) {
        User user = (User)httpSession.getAttribute("user");
        List<Object[]> tuples2 = sessionFactory.getCurrentSession().createNativeQuery(
                queryPaymentCountAndSum)
                .setParameter("projectId", projectId)
                .setParameter("userId", user.getId())
                .list();
        Object[] objects = tuples2.get(0);
        return ((BigInteger)objects[0]).intValue();

    }

    @Override
    public boolean update(Project project) {
        Session currentSession = sessionFactory.getCurrentSession();
        Project newProject = currentSession.get(Project.class, project.getId());
        newProject.setName(project.getName());
        newProject.setPrice(project.getPrice());
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

    @Override
    public double getTotalMoneyOfAllProjects() {
        User user = (User)httpSession.getAttribute("user");

        if (user == null) {
            return 0;
        }

        String query = "select sum(pa.value) from payments pa " +
                "join projects pr on pa.project = pr.id  " +
                "where pr.user_id = :userId AND payment_status = 'SUCCEEDED' ";

        return (Double)sessionFactory.getCurrentSession().createNativeQuery(
                query)
                .setParameter("userId", user.getId())
                .uniqueResult();
    }
}
