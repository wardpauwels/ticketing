package be.ward.ticketing.data.user;

import be.ward.ticketing.entities.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);

}