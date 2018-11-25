package thymeleafexamples.springsecurity.entity;

import javax.persistence.Column;
import java.util.Date;

public class VkUserPaymentDTO {

    public VkUserPaymentDTO(String firstName, String lastName, Date createdAt, Long vkUserId) {
        this.vkUserId = vkUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
    }

    public VkUserPaymentDTO(String firstName, String lastName, Long vkUserId,Date lastPaymentDate ) {
        this.vkUserId = vkUserId;
        this.lastPaymentDate = lastPaymentDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getVkUserId() {
        return vkUserId;
    }

    public void setVkUserId(Long vkUserId) {
        this.vkUserId = vkUserId;
    }

    public Date getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(Date lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    private Long vkUserId;

    private Date lastPaymentDate;

    private String firstName;

    private String lastName;

    private Date createdAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
