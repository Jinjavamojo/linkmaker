package thymeleafexamples.springsecurity.yandex;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduler {

    @Scheduled(fixedDelay = 5000)
    public void checkPendingPayments() {
        int g = 0;
    }
}
