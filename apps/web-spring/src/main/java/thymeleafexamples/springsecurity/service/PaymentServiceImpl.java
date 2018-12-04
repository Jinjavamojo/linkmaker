package thymeleafexamples.springsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import thymeleafexamples.springsecurity.dao.PaymentDao;
import thymeleafexamples.springsecurity.yandex.Payment;
import thymeleafexamples.springsecurity.yandex.PaymentStatus;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Override
    @Transactional(readOnly = true)
    public Payment getPaymentById(String id) {
        return paymentDao.getPaymentById(id);
    }

    @Override
    public void savePayment(Payment payment) {
        paymentDao.savePayment(payment);
    }

    @Override
    public void updatePendingPaymentsStatus() {
        paymentDao.updatePendingPaymentsStatus();
    }

    @Override
    public void setPaymentStatus(PaymentStatus status, String yandexId) {
        paymentDao.setPaymentStatus(status, yandexId);
    }
}
