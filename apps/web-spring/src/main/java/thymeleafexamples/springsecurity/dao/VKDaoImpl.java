package thymeleafexamples.springsecurity.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.entity.Role;
import thymeleafexamples.springsecurity.entity.VkUser;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @Override
    public Long getPaidUsersCount(long projectId) {
        String query = "SELECT count(*)" +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " GROUP BY vk_user.first_name, vk_user.last_name having SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) > 0";
        BigInteger count = null;
        try {
            count = (BigInteger)sessionFactory.getCurrentSession().createNativeQuery(
                    query)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
        return count.longValue();
    }

    @Override
    public Long getUnpaidUsers(long projectId) {
        String query = "SELECT count(*)" +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " GROUP BY vk_user.first_name, vk_user.last_name having SUM(case when p.payment_status = 'CANCELED' OR p.payment_status = 'PENDING' THEN p.value ELSE 0 END) > 0";
        BigInteger count = null;
        try {
            count = (BigInteger)sessionFactory.getCurrentSession().createNativeQuery(
                    query)
                    .setParameter("projectId", projectId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return 0L;
        }
        return count.longValue();
    }

    @Override
    public List<VkUser> getUnpaidUsers(int pageNumber, long projectId) {
        String query = "SELECT first_name, last_name, " +
                " SUM(case when p.payment_status = 'CANCELED' OR p.payment_status = 'PENDING' THEN p.value ELSE 0 END) " +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " GROUP BY vk_user.first_name, vk_user.last_name having SUM(case when p.payment_status = 'CANCELED' OR p.payment_status = 'PENDING' THEN p.value ELSE 0 END) > 0";
        return getUsersByTemplate(query,pageNumber, projectId);
    }

    @Override
    public List<VkUser> getPaidUsers(int pageNumber, long projectId) {

        String query = "SELECT first_name, last_name, " +
                " SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) " +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " GROUP BY vk_user.first_name, vk_user.last_name having SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) > 0";
        return getUsersByTemplate(query,pageNumber,projectId);


//        String quetyStr = String.format()"select first_name, last_name from vk_users limit %s offset %s";
//        this.jdbcTemplate.query(
//                "select first_name, last_name from vk_users",
//                new RowMapper<VkUser>() {
//                    public VkUser mapRow(ResultSet rs, int rowNum) throws SQLException {
//                        VkUser user = new VkUser();
//                        user.setFirstName(rs.getString("first_name"));
//                        user.setLastName(rs.getString("last_name"));
//                        return user;
//                    }
//                });
    }


    private List<VkUser> getUsersByTemplate(String query, int pageNumber, long projectId) {
        if (pageNumber < 0) {
            throw new RuntimeException("Wrong value");
        }
        int firstRes = pageNumber * 10;
        int maxRes = firstRes + 10;
        List<Object[]> tuples = sessionFactory.getCurrentSession().createNativeQuery(
                query)
//                .addEntity("vk_user", VkUser.class )
//                .addJoin( "p", "vk_user.payments")
                .setParameter("projectId", projectId)
                .setMaxResults(maxRes)
                .setFirstResult(firstRes)
                .list();

        List<VkUser> users = new ArrayList<>();
        for(Object[] tuple : tuples) {
            VkUser user = new VkUser();
            user.setFirstName((String)tuple[0]);
            user.setLastName((String)tuple[1]);
            user.setPaymentsSum((Double) tuple[2]);
            users.add(user);
        }
        return users;
    }



}
