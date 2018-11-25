package thymeleafexamples.springsecurity.yandex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import thymeleafexamples.springsecurity.service.PaymentService;

import java.util.List;

@Component
public class PaymentScheduler {


    @Autowired
    private PaymentService paymentService;

    @Scheduled(fixedDelay = 5000)
    public void checkPendingPayments() {
        paymentService.updatePendingPaymentsStatus();
    }
}
