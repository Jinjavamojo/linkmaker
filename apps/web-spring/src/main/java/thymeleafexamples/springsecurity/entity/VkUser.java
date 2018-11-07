package thymeleafexamples.springsecurity.entity;

import org.hibernate.annotations.Cascade;
import thymeleafexamples.springsecurity.yandex.Payment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
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
    private String lastname;

    @OneToMany (mappedBy = "vkUser")
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private List<Payment> payments = new ArrayList<>();

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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
