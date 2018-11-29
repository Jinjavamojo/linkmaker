package thymeleafexamples.springsecurity.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.yandex.Payment;
import thymeleafexamples.springsecurity.yandex.PaymentStatus;
import thymeleafexamples.springsecurity.yandex.YandexKassaComponent;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    private Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private YandexKassaComponent yandexKassaComponent;

    @Value("${paymentsCountFor1Query}")
    private int paymentsCount;

    @Override
    public Payment getPaymentById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Payment> query = currentSession.createQuery("from Payment as p where p.yandexPaymentId = :paymentId", Payment.class);
        query.setParameter("paymentId", id);
        List<Payment> list = query.list();
        if (list.size() > 1) {
            throw new RuntimeException(String.format("Payments with id = %s is more than 1",id));
        }
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    public void updatePendingPaymentsStatus() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Payment> query = currentSession.createQuery("from Payment as p where p.paymentStatus = 'PENDING' order by p.createdAt", Payment.class);
        query.setMaxResults(paymentsCount);
        List<Payment> list = query.list();
        for (Payment payment : list) {
            Payment updatedPayment = yandexKassaComponent.generateGetPaymentInfo(payment.getYandexPaymentId());
            if (updatedPayment != null && updatedPayment.getPaymentStatus() != PaymentStatus.PENDING) {
                payment.setPaymentStatus(updatedPayment.getPaymentStatus());
                payment.setCapturedAt(updatedPayment.getCapturedAt());
                sessionFactory.getCurrentSession().update(payment);
                logger.log(Level.INFO, String.format("payment with id=%s set status=%s", updatedPayment.getYandexPaymentId(), updatedPayment.getPaymentStatus()));
            } else {
                //logger.log(Level.INFO, String.format("payments with id=%s no status changing", updatedPayment.getYandexPaymentId(), updatedPayment.getPaymentStatus()));
            }

        }
    }

    @Override
    public void savePayment(Payment payment) {
        Payment paymentById = getPaymentById(payment.getYandexPaymentId());
        Session currentSession = sessionFactory.getCurrentSession();

        if (paymentById != null) {
            paymentById.setPaymentStatus(payment.getPaymentStatus()); //TODO WHAT FIELDS TO UPDATE
            currentSession.update(paymentById);
        } else {
            if (StringUtils.isEmpty(payment.getProjectName())) {
                throw new RuntimeException(String.format("Couln't save payment=%s without projectName", payment.getYandexPaymentId()));
            }
            Project projectByName = projectDao.getProjectByName(payment.getProjectName());
            payment.setProject(projectByName);
            currentSession.save(payment);
        }
    }
}
