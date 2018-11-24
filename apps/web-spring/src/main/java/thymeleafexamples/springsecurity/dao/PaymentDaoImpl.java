package thymeleafexamples.springsecurity.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.yandex.Payment;

import java.util.List;

@Repository
public class PaymentDaoImpl implements PaymentDao {

    @Autowired
    private SessionFactory sessionFactory;

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

    public List<Payment> getPendingPayments() {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<Payment> query = currentSession.createQuery("from Payment as p where p.paymentStatus = 'PENDING' order by p.createdAt", Payment.class);
        query.setMaxResults(paymentsCount);
        List<Payment> list = query.list();
        return list;
    }

    @Override
    public void savePayment(Payment payment) {
        Payment paymentById = getPaymentById(payment.getYandexPaymentId());
        Session currentSession = sessionFactory.getCurrentSession();

        if (paymentById != null) {
            paymentById.setPaymentStatus(payment.getPaymentStatus()); //TODO WHAT FIELDS TO UPDATE
            currentSession.update(paymentById);

        } else {
            currentSession.save(payment);
        }
    }
}
