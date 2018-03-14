package be.ward.ticketing.entities.tenants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Tenant {

    @Id
    @Column(name = "ID_", nullable = false)
    private String id;

    @Column(name = "REV_")
    private Long rev;

    @Column(name = "NAME_", unique = true)
    private String name;

    public Tenant() {
    }

    public Tenant(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Long getRev() {
        return rev;
    }

    public void setRev(Long rev) {
        this.rev = rev;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
