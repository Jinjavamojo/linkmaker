package thymeleafexamples.springsecurity.entity;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "projects")
public class Project extends AbstractDomainClass {

    @NotBlank(message="{validation.project.name.notBlank}")
    @Column(name = "name")
    private String name;

    @ManyToOne
    @NotBlank(message="{validation.project.user.notBlank}")
    @JoinColumn(name = "userId")
    private User user;

    @NotBlank(message="{validation.project.price.notBlank}")
    @Column(name = "price")
    private Double price;

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
