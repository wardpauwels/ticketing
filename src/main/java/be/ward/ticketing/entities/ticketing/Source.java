package be.ward.ticketing.entities.ticketing;

import javax.persistence.*;

@Entity(name = "source")
public class Source {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lock_version")
    private Integer lockVersion;

    @Column(name = "name")
    private String name;

    public Source() {
    }

    public Source(Long sourceId) {
        this.id = sourceId;
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
