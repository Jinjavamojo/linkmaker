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

    public VkUserPaymentDTO() {
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

    private String vkUserIdString;

    private Date lastPaymentDate;

    private Date paymentCapturedAt;

    private String firstName;

    private String lastName;

    private Date createdAt;

    private String userEmail;

    private String userPhoneNumber;

    private double sumOfPayments;

    public double getSumOfPayments() {
        return sumOfPayments;
    }

    public void setSumOfPayments(double sumOfPayments) {
        this.sumOfPayments = sumOfPayments;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Date getPaymentCapturedAt() {
        return paymentCapturedAt;
    }

    public void setPaymentCapturedAt(Date paymentCapturedAt) {
        this.paymentCapturedAt = paymentCapturedAt;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getVkUserIdString() {
        return vkUserIdString;
    }

    public void setVkUserIdString(String vkUserIdString) {
        this.vkUserIdString = vkUserIdString;
    }
}
