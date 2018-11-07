package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.yandex.Payment;

public interface PaymentService {

    Payment getPaymentById(String id);

    void savePayment(Payment payment);
}
