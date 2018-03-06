package be.ward.ticketing.data;

import be.ward.ticketing.entities.Domain;
import org.jvnet.hk2.config.Dom;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainDao extends CrudRepository<Domain, Long> {
}
