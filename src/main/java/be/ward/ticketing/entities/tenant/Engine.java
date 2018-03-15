package be.ward.ticketing.entities.tenant;

import javax.persistence.*;

@Entity
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Engine() {
    }

    public Long getId() {
        return id;
    }
}
