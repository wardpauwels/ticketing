package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Priority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityRepository extends CrudRepository<Priority, Long> {

    Priority findByNameEquals(String name);

}