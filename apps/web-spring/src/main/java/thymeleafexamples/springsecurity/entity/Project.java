package thymeleafexamples.springsecurity.entity;



import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "projects")
//@NamedQueries({
//        @NamedQuery(name=Project.FIND_ALL, query="select s from Singer s"),
//        @NamedQuery(name=Project.COUNT_ALL, query="select count(s) from Singer s")
//})
public class Project extends AbstractDomainClass {

    @NotBlank(message="{validation.project.name.notBlank}")
    @Column(name = "name")
    private String name;

    @Column(name = "project_description", columnDefinition="text")
    private String projectDescription;

    @Column(name = "project_startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date projectStartDate;

    @Column(name = "autopayment_available")
    private Boolean autoPaymentAvailable;

    @Column(name = "payment_link")
    private String paymentLink;

    @ManyToOne(fetch = FetchType.EAGER)
    //@NotBlank(message="{validation.project.user.notBlank}")
    @JoinColumn(name = "user_id")
    private User user;

    //@NotBlank(message="{validation.project.price.notBlank}")
    @NotNull(message = "{validation.project.price.notBlank}")
    @Column(name = "price")
    private Double price;

    @Transient
    private int uniqueClicksCount = 0;

    @Transient
    private int paidCount = 0;

    @Transient
    private double totalMoneyOfProject = 0d;

    public int getUniqueClicksCount() {
        return uniqueClicksCount;
    }

    public void setUniqueClicksCount(int uniqueClicksCount) {
        this.uniqueClicksCount = uniqueClicksCount;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public void setPaidCount(int paidCount) {
        this.paidCount = paidCount;
    }

    public double getTotalMoneyOfProject() {
        return totalMoneyOfProject;
    }

    public void setTotalMoneyOfProject(double totalMoneyOfProject) {
        this.totalMoneyOfProject = totalMoneyOfProject;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getPaymentLink() {
        return paymentLink;
    }

    public void setPaymentLink(String paymentLink) {
        this.paymentLink = paymentLink;
    }

    public Date getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(Date projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public Boolean getAutoPaymentAvailable() {
        return autoPaymentAvailable;
    }

    public void setAutoPaymentAvailable(Boolean autoPaymentAvailable) {
        this.autoPaymentAvailable = autoPaymentAvailable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
