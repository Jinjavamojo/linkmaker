package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.yandex.Payment;

import java.util.List;

public interface PaymentService {

    Payment getPaymentById(String id);

    void savePayment(Payment payment);

    void updatePendingPaymentsStatus();

}
