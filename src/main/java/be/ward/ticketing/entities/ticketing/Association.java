package be.ward.ticketing.entities.ticketing;

import javax.persistence.*;

@Entity(name = "association")
public class Association {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "lock_version")
    private Integer lockVersion;

    @OneToOne(optional = false)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(name = "association_type")
    private String associationType;

    @OneToOne
    @JoinColumn(name = "association_id")
    private Association association;

    Association() {
    }

    public Association(String associationType, Ticket ticket) {
        this.associationType = associationType;
        this.ticket = ticket;
    }

    public Association(Long id, String associationType, Ticket ticket) {
        this(associationType, ticket);
        this.id = id;
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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getAssociationType() {
        return associationType;
    }

    public void setAssociationType(String associationType) {
        this.associationType = associationType;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }
}