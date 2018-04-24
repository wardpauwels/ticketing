package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.AssignService;
import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/assigns")
public class AssignController {

    private final RabbitTemplate rabbitTemplate;

    private final AssignService assignService;

    @Autowired
    public AssignController(RabbitTemplate rabbitTemplate, AssignService assignService) {
        this.rabbitTemplate = rabbitTemplate;
        this.assignService = assignService;
    }

    @PostMapping("/usertoticket")
    public String assignUserToTicket(@RequestParam String ticketId, @RequestParam String assignedUser) {
        Ticket ticket = assignService.addResolverToTicket(Long.valueOf(ticketId), assignedUser);

        if (ticket != null) {
            rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_RESOLVER_ADDED, ticket.getId());
            return makeJsonObject("User has been added to ticket");
        } else {
            rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_RESOLVER_NOT_FOUND, assignedUser);
            return makeJsonObject("User was not found");
        }
    }

    String makeJsonObject(String resultString) {
        return new JSONObject()
                .put("result", resultString)
                .toString();
    }
}
