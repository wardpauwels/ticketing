package be.ward.ticketing.data;

import be.ward.ticketing.entities.Priority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriorityDao extends CrudRepository<Priority, Long> {
}
