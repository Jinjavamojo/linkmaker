package thymeleafexamples.springsecurity.dao;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.entity.Role;
import thymeleafexamples.springsecurity.entity.VkUser;

import java.util.List;

@Repository
public class VKDaoImpl implements VKDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public VkUser getUserById(Long id) {
        Query<VkUser> theQuery = sessionFactory.getCurrentSession().createQuery("from VkUser where vkUserId=:vkUserId", VkUser.class);
        theQuery.setParameter("vkUserId",id);
        List<VkUser> list = theQuery.list();
        if (list.size() > 1) {
            throw new RuntimeException(String.format("Users with id %s is more than 1", id));
        }
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public void saveUserIfNotExists(VkUser user) {
        VkUser userById = getUserById(user.getVkUserId());
        if (userById == null) {
            sessionFactory.getCurrentSession().saveOrUpdate(user);
        }
    }
}
