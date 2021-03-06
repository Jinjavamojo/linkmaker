package thymeleafexamples.springsecurity.yandex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cascade;
import org.springframework.lang.Nullable;
import thymeleafexamples.springsecurity.entity.AbstractDomainClass;
import thymeleafexamples.springsecurity.entity.Project;
import thymeleafexamples.springsecurity.entity.User;
import thymeleafexamples.springsecurity.entity.VkUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "payments")
public class Payment {

    @Id
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
    private Date capturedAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "description")
    private String description;

    @Embedded
    private PaymentMethod paymentMethod;

    @Embedded
    private PaymentCancelationDetails paymentCancelationDetails;

    @Embedded
    private Recipient recipient;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "value", column = @Column(name = "refunded_amount_value")),
                    @AttributeOverride(name = "currency", column = @Column(name = "refunded_amount_currency"))
            })
    private Amount refundedAmount;

    @Column(name = "test")
    private Boolean test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vk_user")
    private VkUser vkUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.REFRESH)
    @JoinColumn(name = "project")
    //TODO NULL OR NOT NULL? HOW WE PLAN TO IMPORT DATAS?
    private Project project;

    @Transient
    @Nullable
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public PaymentCancelationDetails getPaymentCancelationDetails() {
        return paymentCancelationDetails;
    }

    public void setPaymentCancelationDetails(PaymentCancelationDetails paymentCancelationDetails) {
        this.paymentCancelationDetails = paymentCancelationDetails;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public VkUser getVkUser() {
        return vkUser;
    }

    public void setVkUser(VkUser vkUser) {
        this.vkUser = vkUser;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public String getYandexPaymentId() {
        return yandexPaymentId;
    }

    public void setYandexPaymentId(String yandexPaymentId) {
        this.yandexPaymentId = yandexPaymentId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public AuthorizationDetails getAuthorizationDetails() {
        return authorizationDetails;
    }

    public void setAuthorizationDetails(AuthorizationDetails authorizationDetails) {
        this.authorizationDetails = authorizationDetails;
    }

    public Date getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(Date capturedAt) {
        this.capturedAt = capturedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Amount getRefundedAmount() {
        return refundedAmount;
    }

    public void setRefundedAmount(Amount refundedAmount) {
        this.refundedAmount = refundedAmount;
    }

    public Boolean getTest() {
        return test;
    }

    public void setTest(Boolean test) {
        this.test = test;
    }
}
