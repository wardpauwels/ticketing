package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long> {

    Iterable<Ticket> findByAssignedUserIsNull();

    @Override
    @RestResource(exported = false)
    void delete(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Ticket ticket);

    Iterable<Ticket> findByAssignedUser(String username);
}