package be.ward.ticketing.data.ticketing;

import be.ward.ticketing.entities.ticketing.Association;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationDao extends CrudRepository<Association, Long> {
}