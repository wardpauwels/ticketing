package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Source;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceDao extends CrudRepository<Source, Long> {
}