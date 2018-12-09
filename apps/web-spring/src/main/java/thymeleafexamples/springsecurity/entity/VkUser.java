package thymeleafexamples.springsecurity.entity;

import org.hibernate.annotations.Cascade;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "vk_users")
public class VkUser {

    @Id
    private Long vkUserId;

    @Column(name = "alias")
    private String alias;

    @Column(name = "first_action_date")
    private Date firstDateAppear;

    @Column(name = "last_action_date")
    private Date lastDateAppear;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @OneToMany (mappedBy = "vkUser",fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Payment> payments = new ArrayList<>();

    @Transient
    private Double paymentsSum = 0D;

    public Double getPaymentsSum() {
        return paymentsSum;
    }

    public void setPaymentsSum(Double paymentsSum) {
        this.paymentsSum = paymentsSum;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Long getVkUserId() {
        return vkUserId;
    }

    public void setVkUserId(Long vkUserId) {
        this.vkUserId = vkUserId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Date getFirstDateAppear() {
        return firstDateAppear;
    }

    public void setFirstDateAppear(Date firstDateAppear) {
        this.firstDateAppear = firstDateAppear;
    }

    public Date getLastDateAppear() {
        return lastDateAppear;
    }

    public void setLastDateAppear(Date lastDateAppear) {
        this.lastDateAppear = lastDateAppear;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
