package be.ward.ticketing.data;

import be.ward.ticketing.entities.Association;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationDao extends CrudRepository<Association, Long> {
}
