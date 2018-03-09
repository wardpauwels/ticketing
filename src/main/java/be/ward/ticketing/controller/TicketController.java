package be.ward.ticketing.controller;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Messages;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    private TicketingService ticketingService;

    @PostMapping("/ticket")
    public Ticket createNewTicket(@RequestParam String username, @RequestParam String message) {
        Ticket ticket = ticketingService.createTicket(username, message);
        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_NEW_TICKET, ticket.getId());
        return ticket;
    }

    @GetMapping("/ticket/{ticketId}")
    public Ticket findTicketWithId(@PathVariable String ticketId) {
        return ticketingService.findTicket(Long.valueOf(ticketId));
    }

    @GetMapping("/tickets")
    public List<Ticket> findAllTickets() {
        List<Ticket> tickets = new ArrayList<>();

        for (Ticket ticket : ticketingService.findAllTickets()) {
            tickets.add(ticket);
        }
        return tickets;
    }

    @GetMapping("/ticketswithoutresolver")
    public List<Ticket> findTicketsWithoutResolver() {
        List<Ticket> tickets = new ArrayList<>();

        for (Ticket ticket : ticketingService.findTicketsWithoutResolver()) {
            tickets.add(ticket);
        }

        return tickets;
    }

    @GetMapping("/ticketsforuser")
    public List<Ticket> getTicketForResolver(@PathVariable String username) {
        List<Ticket> tickets = new ArrayList<>();

        for (Ticket ticket : ticketingService.findTicketsForResolver(username)) {
            tickets.add(ticket);
        }

        return tickets;
    }

    @PostMapping("/ticket/{ticketId}/answer")
    public void answerOnTicketWithId(@PathVariable String ticketId, @RequestParam String answer) {
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));
        ticket.setTopicText(answer);
        ticket = ticketingService.saveTicket(ticket);
        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_TICKET_ANSWERED, ticket.getId());
    }
}
