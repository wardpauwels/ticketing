package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Messages;
import org.json.JSONObject;
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
    public String assignUserToTicket(@RequestParam String ticketId, @RequestParam String assignedUser) {
        Ticket ticket = ticketingService.addResolverToTicket(Long.valueOf(ticketId), assignedUser);

        if (ticket != null) {
            rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_ADDED, ticket.getId());
            return makeJsonOject("User has been added to ticket");
        } else {
            rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_NOT_FOUND, assignedUser);
            return makeJsonOject("User was not found");
        }
    }

    private String makeJsonOject(String resultString) {
        return new JSONObject()
                .put("result", resultString)
                .toString();
    }
}
