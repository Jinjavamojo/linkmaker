package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.yandex.Payment;

import java.util.List;

public interface PaymentDao {

    Payment getPaymentById(String id);

    void savePayment(Payment payment);

    List<Payment> getPendingPayments();
}
