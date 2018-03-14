package be.ward.ticketing.entities.tenants;

import javax.persistence.*;

@Entity
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "engine_id")
    private int id;

    public Engine() {
    }

    public int getId() {
        return id;
    }
}
