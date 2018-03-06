package be.ward.ticketing.data;

import be.ward.ticketing.entities.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends CrudRepository<Ticket, Long> {

    public Iterable<Ticket> findByAssignedUserIsNull();

}
