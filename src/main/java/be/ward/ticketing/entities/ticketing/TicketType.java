package be.ward.ticketing.entities.ticketing;

import javax.persistence.*;

@Entity(name = "ticket_type")
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lock_version")
    private Integer lockVersion;

    @Column(name = "name")
    private String name;

    public TicketType() {
    }

    public TicketType(Long ticketTypeId) {
        this.id = ticketTypeId;
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
