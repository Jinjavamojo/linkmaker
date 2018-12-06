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
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
        String query = "select count(*) from (" +
                "            SELECT \n" +
                "        vk_user.first_name\n" +
                "        last_name,\n" +
                "        vk_user.vkuserid\n" +
                "    FROM\n" +
                "        vk_users vk_user\n" +
                "    INNER JOIN\n" +
                "        payments p\n" +
                "            ON p.vk_user = vk_user.vkuserid\n" +
                "    where\n" +
                "        p.project = :projectId\n" +
                "    GROUP BY\n" +
                "        vk_user.first_name,\n" +
                "        vk_user.last_name,\n" +
                "        vk_user.vkuserid\n" +
                "    having\n" +
                "        SUM(case\n" +
                "            when p.payment_status = 'WAITING_FOR_CAPTURE'\n" +
                "            OR p.payment_status = 'SUCCEEDED' THEN p.value\n" +
                "            ELSE 0\n" +
                "        END) > 0 limit 10\n" +
                ")  as t\n";
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
    public Long getLinkedUsersCount(long projectId) {
        String query = "SELECT count(*)" +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " and (p.payment_status = 'CANCELED' OR p.payment_status = 'PENDING')";
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
    public List<VkUserPaymentDTO> getLinkedUsers(int pageNumber, long projectId) {
        String query = "SELECT first_name, last_name, p.created_at, vk_user.vkuserid " +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " and (p.payment_status = 'CANCELED' OR p.payment_status = 'PENDING')";
        return getLinkedUsers(query,pageNumber, projectId);
    }

    @Override
    public List<VkUser> getPaidUsers(int pageNumber, long projectId) {

        String query = "SELECT first_name, last_name, vk_user.vkuserid, " +
                " SUM(case when p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) " +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " GROUP BY vk_user.first_name, vk_user.last_name, vk_user.vkuserid having SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) > 0";
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
            user.setVkUserId(((BigInteger) tuple[2]).longValue());
            user.setPaymentsSum((Double) tuple[3]);
            users.add(user);
        }
        return users;
    }


    private List<VkUserPaymentDTO> getLinkedUsers(String query, int pageNumber, long projectId) {
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

        List<VkUserPaymentDTO> users = new ArrayList<>();
        for(Object[] tuple : tuples) {
            String firstName = (String) tuple[0];
            String lastName = (String) tuple[1];
            Date paymentDateCreatedAt = (Date) tuple[2];

            Long vkUserId = ((BigInteger) tuple[3]).longValue();
            VkUserPaymentDTO dto = new VkUserPaymentDTO(firstName,lastName,paymentDateCreatedAt,vkUserId);
            users.add(dto);
        }
        return users;
    }



}
