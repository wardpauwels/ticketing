package be.ward.ticketing.data.user;

import be.ward.ticketing.entities.user.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {

    Role findByRole(String role);

}
