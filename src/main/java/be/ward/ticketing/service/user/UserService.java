package be.ward.ticketing.service.user;

import be.ward.ticketing.data.user.UserRepository;
import be.ward.ticketing.entities.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User createUser(String username, String password) {
        User user = new User(username, encoder.encode(password));
        return userRepository.save(user);
    }

    public Iterable<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserWithUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}