package thymeleafexamples.springsecurity.dao;

import thymeleafexamples.springsecurity.yandex.Payment;

public interface PaymentDao {

    Payment getPaymentById(String id);

    void savePayment(Payment payment);
}
