package be.ward.ticketing.entities.ticketing;

import javax.persistence.*;

@Entity(name = "domain")
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lock_version")
    private Integer lockVersion;

    @Column(name = "name")
    private String name;

    public Domain() {
    }

    public Domain(Long domainId) {
        this.id = domainId;
    }

    public Long getId() {
        return id;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
