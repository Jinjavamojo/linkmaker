package thymeleafexamples.springsecurity.yandex;

import thymeleafexamples.springsecurity.entity.AbstractDomainClass;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.xml.crypto.Data;

@Entity
@Table(name = "payments")
public class Payment extends AbstractDomainClass {

    @Column(name = "yandex_payment_id")
    private String yandexPaymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "paid")
    private Boolean paid;

    @Embedded
    private Amount amount;

    @Embedded
    private AuthorizationDetails authorizationDetails;

    @Column(name = "captured_at")
    private Data capturedAt;

    @Column(name = "created_at")
    private Data createdAt;

    @Column(name = "description")
    private String description;




}
