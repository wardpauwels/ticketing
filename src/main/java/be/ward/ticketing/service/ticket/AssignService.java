package be.ward.ticketing.service.ticket;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignService {
    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public AssignService(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public Ticket addResolverToTicket(Long ticketId, String username) {
        Ticket ticket = ticketService.findTicket(ticketId);
        User user = userService.findUserWithUsername(username);

        if (user != null) {
            ticket.setAssignedUser(username);
            return ticketService.saveTicket(ticket);
        } else return null;
    }
}