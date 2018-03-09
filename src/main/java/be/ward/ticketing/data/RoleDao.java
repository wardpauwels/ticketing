package be.ward.ticketing.data;

import be.ward.ticketing.entities.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

    Role findByRole(String role);

}
