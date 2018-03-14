package be.ward.ticketing.controller.user;

import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.service.TicketingService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        for (User user : ticketingService.findAllUsers()) {
            users.add(user);
        }

        return users;
    }

    @PostMapping("/user")
    public User makeNewUser(@RequestParam String username, @RequestParam String password) {
        return ticketingService.createUser(username, password);
    }

}
