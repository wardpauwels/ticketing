package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Messages;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AssignController {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketingService ticketingService;

    @PostMapping("/assignusertoticket")
    public void assignUserToTicket(@RequestParam String ticketId, @RequestParam String assignedUser) {
        User user = ticketingService.findUserWithUsername(assignedUser);

        if (user != null) {
            Ticket ticket = ticketingService.addResolverToTicket(Long.valueOf(ticketId), user.getUsername());
            rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_ADDED, ticket.getId());
        } else {
            rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_NOT_FOUND, assignedUser);
        }


    }
}
