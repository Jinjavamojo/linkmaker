package thymeleafexamples.springsecurity.service;

import thymeleafexamples.springsecurity.yandex.Payment;
import thymeleafexamples.springsecurity.yandex.PaymentStatus;

import java.util.List;

public interface PaymentService {

    Payment getPaymentById(String id);

    void savePayment(Payment payment);

    void updatePendingPaymentsStatus();

    void setPaymentStatus(PaymentStatus status, String yandexId);

}
