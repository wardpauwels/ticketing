package be.ward.ticketing.data.user;

import be.ward.ticketing.entities.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}