package thymeleafexamples.springsecurity.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Table(name = "projects")
public class Project extends AbstractDomainClass {

    @NotBlank(message="{validation.project.name.notBlank}")
    @Column(name = "name")
    private String name1;

    @Column(name = "desc")
    private String desc;

    @Column(name = "date")
    private Date projectStartDate;

    @Column(name = "autoPaymentAvailable")
    private Boolean autoPaymentAvailable;

    @ManyToOne
    @NotBlank(message="{validation.project.user.notBlank}")
    @JoinColumn(name = "userId")
    private User user;

    @NotBlank(message="{validation.project.price.notBlank}")
    @Column(name = "price")
    private Double price;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
