package be.ward.ticketing.service;

import be.ward.ticketing.entities.*;
import org.springframework.stereotype.Service;

@Service
public interface TicketingService {

    Ticket createTicket(String username, String message);


    Iterable<Ticket> findAllTickets();

    Iterable<Ticket> findTicketsWithoutResolver();

    Domain findDomainWithId(long id);

    Source findSourceWithId(long id);

    TicketType findTicketTypeWithId(long id);

    Topic findTopicWithId(long id);

    Ticket findTicket(Long id);

    Ticket saveTicket(Ticket ticket);
}
