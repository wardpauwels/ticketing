package be.ward.ticketing.data;

import be.ward.ticketing.entities.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDao extends CrudRepository<Ticket, Long> {

    Iterable<Ticket> findByAssignedUserIsNull();

    @Override
    @RestResource(exported = false)
    void delete(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Ticket ticket);

    Iterable<Ticket> findByAssignedUser(String username);
}
