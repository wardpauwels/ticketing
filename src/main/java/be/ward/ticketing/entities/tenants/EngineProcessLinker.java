package be.ward.ticketing.entities.tenants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class EngineProcessLinker {

    @Column
    @ManyToOne
    Engine engine;

}
