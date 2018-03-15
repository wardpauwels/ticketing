package be.ward.ticketing.entities.tenant;

import javax.persistence.*;

@Entity
public class Process {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

}
