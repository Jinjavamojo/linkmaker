package thymeleafexamples.springsecurity.entity;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project extends AbstractDomainClass {

    @NotBlank(message="{validation.project.name.notBlank}")
    @Column(name = "name")
    private String name;

    @Column(name = "project_description", columnDefinition="text")
    private String projectDescription;

    @Column(name = "project_startDate")
    private Date projectStartDate;

    @Column(name = "autopayment_available")
    private Boolean autoPaymentAvailable;

    @Column(name = "payment_link")
    private String paymentLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotBlank(message="{validation.project.user.notBlank}")
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message="{validation.project.price.notBlank}")
    @Column(name = "price")
    private Double price;

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
