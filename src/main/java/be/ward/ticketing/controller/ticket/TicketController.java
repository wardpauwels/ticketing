package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.AssociationService;
import be.ward.ticketing.service.ticket.TicketService;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final RabbitTemplate rabbitTemplate;
    private final TicketService ticketService;
    private final AssociationService associationService;

    @Autowired
    public TicketController(RabbitTemplate rabbitTemplate,
                            TicketService ticketService,
                            AssociationService associationService) {
        this.rabbitTemplate = rabbitTemplate;
        this.ticketService = ticketService;
        this.associationService = associationService;
    }

    @GetMapping
    public List<Ticket> findAllTickets() {
        return Lists.newArrayList(ticketService.findAllTickets());
    }

    @GetMapping("/{ticketId}")
    public Ticket findTicketWithId(@PathVariable String ticketId) {
        return ticketService.findTicket(Long.valueOf(ticketId));
    }

    @GetMapping("/noresolver")
    public List<Ticket> findTicketsWithoutResolver() {
        return Lists.newArrayList(ticketService.findTicketsWithoutResolver());
    }

    @GetMapping("/user/{username}")
    public List<Ticket> getTicketForResolver(@PathVariable String username) {
        return Lists.newArrayList(ticketService.findTicketsForResolver(username));
    }

    @PostMapping
    public Ticket createNewTicket(@RequestParam String username, @RequestParam String message) {
        Ticket ticket = ticketService.createTicket(username, message);
        Association association = associationService.createAssociation(AssociationTypes.ticket, ticket);

        rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_NEW_TICKET, association.getId());
        return ticket;
    }

    @PostMapping("/answers/{ticketId}")
    public Ticket answerOnTicketWithId(@PathVariable String ticketId, @RequestParam String user, @RequestParam String answer) {
        Ticket oldTicket = ticketService.findTicket(Long.valueOf(ticketId));
        Association latestAssociation = associationService.getLastAssociationFromTicket(oldTicket);
        Ticket newTicket = ticketService.answerOnTicketWithId(latestAssociation.getTicket().getId(), user, answer);

        rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_TICKET_ANSWERED, oldTicket.getId());
        return newTicket;
    }

    @PostMapping("/solved/{ticketId}")
    public Ticket ticketSolved(@PathVariable String ticketId) {
        Association association = associationService.getTopAssociationFromTicket(ticketService.findTicket(Long.valueOf(ticketId)));
        Ticket ticket = association.getTicket();

        rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_PROBLEM_SOLVED, ticket.getId());
        return ticket;
    }

    @PostMapping("/notsolved/{ticketId}")
    public Ticket ticketNotSolved(@PathVariable String ticketId, @RequestParam String comment) {
        Association association = associationService.getTopAssociationFromTicket(ticketService.findTicket(Long.valueOf(ticketId)));
        Ticket ticket = association.getTicket();

        rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_PROBLEM_NOT_SOLVED, new Object[]{ticket.getId(), comment});
        return ticket;
    }
}