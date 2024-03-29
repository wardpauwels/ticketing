package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Domain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainDao extends CrudRepository<Domain, Long> {
}
