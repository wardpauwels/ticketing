package be.ward.ticketing.service;

import be.ward.ticketing.entities.ticketing.*;
import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.entities.user.User;
import org.springframework.stereotype.Service;

@Service
public interface TicketingService {


    // TICKETS
    Ticket createTicket(String username, String message);

    Ticket findTicket(Long id);

    Iterable<Ticket> findTicketsWithoutResolver();

    Iterable<Ticket> findAllTickets();

    Ticket saveTicket(Ticket ticket);

    Ticket addResolverToTicket(Long ticketId, String assignedUser);

    Domain findDomainWithId(Long id);

    Priority findPriorityByName(String name);

    Source findSourceWithId(Long id);

    TicketType findTicketTypeWithId(Long id);

    Topic findTopicWithId(Long id);

    Iterable<Ticket> findTicketsForResolver(String username);

    User createUser(String username, String password);

    Iterable<User> findAllUsers();

    User findUserWithUsername(String username);

    Iterable<Domain> findAllDomains();

    Iterable<Priority> findAllPriorities();

    Iterable<Role> findAllRoles();

    Iterable<Source> findAllSources();

    Iterable<TicketType> findAllTicketTypes();

    Iterable<Topic> findAllTopics();

    Priority findPriorityWithId(Long id);
}
