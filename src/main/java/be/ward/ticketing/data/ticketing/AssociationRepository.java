package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends CrudRepository<Association, Long> {

    Association findByTicket(Ticket ticket);

    Association findByAssociation(Association association);

}