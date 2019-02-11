package thymeleafexamples.springsecurity.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import thymeleafexamples.springsecurity.entity.VkUser;
import thymeleafexamples.springsecurity.entity.VkUserPaymentDTO;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class VKDaoImpl implements VKDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Environment env;


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
            sessionFactory.getCurrentSession().save(user);
        } else {
            String resultEmail = StringUtils.isNotEmpty(user.getEmail()) ? user.getEmail() : userById.getEmail();
            userById.setEmail(resultEmail);
            sessionFactory.getCurrentSession().update(userById);
        }
    }

    @Override
    public Long getPaidUsersCount(long projectId) {
        String query = "SELECT count(*)" +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and (p.payment_status = 'SUCCEEDED') " +
                "and p.value >= pr.price";
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
    public Long getParticallyPaidUserCount(long projectId) {
        String query = "SELECT count(*)" +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and (p.payment_status = 'SUCCEEDED') " +
                "and p.value < pr.price";
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
    public List<VkUserPaymentDTO> getPaidUsers(long projectId) {
        String query = "SELECT vk_user.first_name,vk_user.last_name, CAST(vk_user.vkuserid AS varchar(255)), vk_user.email, p.captured_at " +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and p.payment_status = 'SUCCEEDED' and p.value >= pr.price";
        List<Object[]> res = sessionFactory.getCurrentSession().createNativeQuery(
                query)
                .setParameter("projectId", projectId)
                .list();

        List<VkUserPaymentDTO> users = new ArrayList<>();
        for(Object[] tuple : res) {
            VkUserPaymentDTO user = new VkUserPaymentDTO();
            user.setFirstName((String)tuple[0]);
            user.setLastName((String)tuple[1]);
            user.setVkUserIdString((String)(tuple[2]));
            user.setUserEmail((String)tuple[3]);
            user.setPaymentCapturedAt((Date) tuple[4]);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<VkUserPaymentDTO> getPaidUsersByPage(int pageNumber, long projectId) {

        String query = "SELECT first_name, last_name, vk_user.vkuserid, vk_user.phone_number, vk_user.email,SUM(case when p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END)" +
        "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
        "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
        "and (p.payment_status = 'SUCCEEDED') " +
        "GROUP BY vk_user.first_name, vk_user.last_name, vk_user.vkuserid " +
        "having SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) > 0";
        return getUsersByTemplate(query, pageNumber, projectId);
    }

    @Override
    public List<String> getPaidUserIds(long projectId) {

        String query = "SELECT CAST(vk_user.vkuserid AS varchar(255)) " +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and p.payment_status = 'SUCCEEDED' and p.value >= pr.price";
        List<String> res = sessionFactory.getCurrentSession().createNativeQuery(
                query)
                .setParameter("projectId", projectId)
                .list();
        return res;
    }

//    @Override
//    public List<VkUser> getPaidUsers(int pageNumber, long projectId) {
//
//        String query = "SELECT first_name, last_name, vk_user.vkuserid, " +
//                " SUM(case when p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) " +
//                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
//                " GROUP BY vk_user.first_name, vk_user.last_name, vk_user.vkuserid having SUM(case when p.payment_status = 'WAITING_FOR_CAPTURE' OR p.payment_status = 'SUCCEEDED' THEN p.value ELSE 0 END) > 0";
//        return getUsersByTemplate(query,pageNumber,projectId);
    //}


    private List<VkUserPaymentDTO> getUsersByTemplate(String query, @Nullable Integer pageNumber, long projectId) {
        int userPerPageDisplayed = Integer.valueOf(env.getProperty("userPerPageDisplayed"));
        if (pageNumber < 0) {
            throw new RuntimeException("Wrong value");
        }
        int firstRes = pageNumber * userPerPageDisplayed;
        List<Object[]> tuples = sessionFactory.getCurrentSession().createNativeQuery(
                query)
//                .addEntity("vk_user", VkUser.class )
//                .addJoin( "p", "vk_user.payments")
                .setParameter("projectId", projectId)
                .setMaxResults(userPerPageDisplayed)
                .setFirstResult(firstRes)
                .list();

        List<VkUserPaymentDTO> users = new ArrayList<>();
        for(Object[] tuple : tuples) {
            VkUserPaymentDTO user = new VkUserPaymentDTO();
            user.setFirstName((String)tuple[0]);
            user.setLastName((String)tuple[1]);
            user.setVkUserId(((BigInteger) tuple[2]).longValue());
            user.setUserPhoneNumber((String)tuple[3]);
            user.setUserEmail((String)tuple[4]);
            user.setSumOfPayments((Double) tuple[5]);

            //user.setPaymentsSum((Double) tuple[3]);
            users.add(user);
        }
        return users;
    }


    private List<VkUserPaymentDTO> getLinkedUsersByPage(String query, int pageNumber, long projectId) {
        int userPerPageDisplayed = Integer.valueOf(env.getProperty("userPerPageDisplayed"));

        if (pageNumber < 0) {
            throw new RuntimeException("Wrong value");
        }
        int firstRes = pageNumber * userPerPageDisplayed;
        List<Object[]> tuples = sessionFactory.getCurrentSession().createNativeQuery(
                query)
//                .addEntity("vk_user", VkUser.class )
//                .addJoin( "p", "vk_user.payments")
                .setParameter("projectId", projectId)
                .setMaxResults(userPerPageDisplayed)
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



    @Override
    public List<VkUserPaymentDTO> getVisitedUsers(long projectId) {
        String query = "SELECT vk_user.first_name,vk_user.last_name, CAST(vk_user.vkuserid AS varchar(255)), vk_user.email, p.captured_at " +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and p.payment_status != 'SUCCEEDED' ";
        List<Object[]> res = sessionFactory.getCurrentSession().createNativeQuery(
                query)
                .setParameter("projectId", projectId)
                .list();

        List<VkUserPaymentDTO> users = new ArrayList<>();
        for(Object[] tuple : res) {
            VkUserPaymentDTO user = new VkUserPaymentDTO();
            user.setFirstName((String)tuple[0]);
            user.setLastName((String)tuple[1]);
            user.setVkUserIdString((String)(tuple[2]));
            user.setUserEmail((String)tuple[3]);
            user.setPaymentCapturedAt((Date) tuple[4]);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<String> getVisitedUserIds(long projectId) {

        String query = "SELECT CAST(vk_user.vkuserid AS varchar(255)) " +
                "FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid " +
                "JOIN projects pr on pr.id = p.project where p.project = :projectId " +
                "and p.payment_status != 'SUCCEEDED' ";
        List<String> res = sessionFactory.getCurrentSession().createNativeQuery(
                query)
                .setParameter("projectId", projectId)
                .list();
        return res;
    }

    @Override
    public Long getVisitedUsersCount(long projectId) {
        String query = "SELECT count(*)" +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " and p.payment_status != 'SUCCEEDED' ";
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
    public List<VkUserPaymentDTO> getVisitedUsersByPage(int pageNumber, long projectId) {
        String query = "SELECT first_name, last_name, p.created_at, vk_user.vkuserid " +
                " FROM vk_users vk_user JOIN payments p ON p.vk_user = vk_user.vkuserid where p.project = :projectId " +
                " and p.payment_status != 'SUCCEEDED'  ";
        return getLinkedUsersByPage(query,pageNumber, projectId);
    }
}
