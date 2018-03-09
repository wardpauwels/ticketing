package be.ward.ticketing.data;

import be.ward.ticketing.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {

    User findUserByUsername(String username);

}
