package thymeleafexamples.springsecurity.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by jt on 12/16/15.
 */
@MappedSuperclass
public class AbstractDomainClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Version
    private Integer version;

    @Column(name = "db_date_created")
    private Date dbDateCreated;

    @Column(name = "db_last_updated")
    private Date dbLastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDbDateCreated(Date dbDateCreated) {
        this.dbDateCreated = dbDateCreated;
    }

    public void setDbLastUpdated(Date dbLastUpdated) {
        this.dbLastUpdated = dbLastUpdated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getDbDateCreated() {
        return dbDateCreated;
    }

    public Date getDbLastUpdated() {
        return dbLastUpdated;
    }

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        dbLastUpdated = new Date();
        if (dbDateCreated ==null) {
            dbDateCreated = new Date();
        }
    }
}
