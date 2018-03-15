package be.ward.ticketing.entities.tenant;

import javax.persistence.*;

@Entity
public class EngineProcessLinker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    Engine engine;


}
