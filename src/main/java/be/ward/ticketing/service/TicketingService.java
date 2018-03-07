package be.ward.ticketing.service;

import be.ward.ticketing.entities.*;
import org.springframework.stereotype.Service;

@Service
public interface TicketingService {

    Ticket createTicket(String username, String message);

    Iterable<Ticket> findAllTickets();

    Iterable<Ticket> findTicketsWithoutResolver();

    Domain findDomainWithId(Long id);

    Source findSourceWithId(Long id);

    TicketType findTicketTypeWithId(Long id);

    Topic findTopicWithId(Long id);

    Ticket findTicket(Long id);

    Ticket saveTicket(Ticket ticket);

    Ticket addResolverToTicket(Long ticketId, String assignedUser);

    Priority findPriorityByName(String variable);
}
