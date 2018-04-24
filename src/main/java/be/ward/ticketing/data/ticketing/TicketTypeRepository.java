package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.TicketType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketTypeRepository extends CrudRepository<TicketType, Long> {
}