package be.ward.ticketing.controller;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
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
public class AssignerController {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketingService ticketingService;

    @PostMapping("/assignusertoticket")
    public void assignUserToTicket(@RequestParam String ticketId, @RequestParam String assignedUser) {
        Ticket ticket = ticketingService.addResolverToTicket(Long.valueOf(ticketId), assignedUser);
        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_ADDED, ticket.getId());
    }
}
