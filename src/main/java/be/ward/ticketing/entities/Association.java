package be.ward.ticketing.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "association")
public class Association implements Serializable {

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

    @OneToOne(optional = false)
    @JoinColumn(name = "association_id")
    private Association association;


}
