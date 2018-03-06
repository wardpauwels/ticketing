package be.ward.ticketing.data;

import be.ward.ticketing.entities.TicketType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeDao extends CrudRepository<TicketType, Long> {
}
