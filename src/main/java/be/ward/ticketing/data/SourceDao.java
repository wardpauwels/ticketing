package be.ward.ticketing.data;

import be.ward.ticketing.entities.Source;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourceDao extends CrudRepository<Source, Long> {
}
