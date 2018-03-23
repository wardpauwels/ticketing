package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.Messages;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/ticket/{ticketId}")
    public Ticket findTicketWithId(@PathVariable String ticketId) {
        return ticketingService.findTicket(Long.valueOf(ticketId));
    }

    @GetMapping("/tickets")
    public List<Ticket> findAllTickets() {
        return Lists.newArrayList(ticketingService.findAllTickets());
    }

    @GetMapping("/ticketswithoutresolver")
    public List<Ticket> findTicketsWithoutResolver() {
        return Lists.newArrayList(ticketingService.findTicketsWithoutResolver());
    }

    @GetMapping("/ticketsforuser")
    public List<Ticket> getTicketForResolver(@PathVariable String username) {
        return Lists.newArrayList(ticketingService.findTicketsForResolver(username));
    }

    @PostMapping("/ticket")
    public Ticket createNewTicket(@RequestParam String username, @RequestParam String message) {
        Ticket ticket = ticketingService.createTicket(username, message);
        Association association = ticketingService.createAssociation(AssociationTypes.ticket, ticket);

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_NEW_TICKET, association.getId());
        return ticket;
    }

    @PostMapping("/ticket/{ticketId}/answer")
    public Ticket answerOnTicketWithId(@PathVariable String ticketId, @RequestParam String user, @RequestParam String answer) {
        Ticket oldTicket = ticketingService.findTicket(Long.valueOf(ticketId));
        Association latestAssociation = ticketingService.getLastAssociationFromTicket(oldTicket);
        Ticket newTicket = ticketingService.answerOnTicketWithId(latestAssociation.getTicket().getId(), user, answer);

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_TICKET_ANSWERED, oldTicket.getId());
        return newTicket;
    }

    @PostMapping("/ticketsolved/{ticketId}")
    public Ticket ticketSolved(@PathVariable String ticketId) {
        Association association = ticketingService.getTopAssociationFromTicket(ticketingService.findTicket(Long.valueOf(ticketId)));
        Ticket ticket = association.getTicket();

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_PROBLEM_SOLVED, ticket.getId());
        return ticket;
    }

    @PostMapping("/ticketnotsolved/{ticketId}")
    public Ticket ticketWasNotSolved(@PathVariable String ticketId, @RequestParam String comment) {
        Association association = ticketingService.getTopAssociationFromTicket(ticketingService.findTicket(Long.valueOf(ticketId)));
        Ticket ticket = association.getTicket();

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_PROBLEM_NOT_SOLVED, new Object[]{ticket.getId(), comment});
        return ticket;
    }
}