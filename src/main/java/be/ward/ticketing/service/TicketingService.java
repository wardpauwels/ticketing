package be.ward.ticketing.service;

import be.ward.ticketing.entities.ticketing.*;
import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.entities.user.User;

public interface TicketingService {

    // TICKETS
    Ticket createTicket(String username, String message);

    Ticket findTicket(Long id);

    Iterable<Ticket> findTicketsWithoutResolver();

    Iterable<Ticket> findAllTickets();

    void saveTicket(Ticket ticket);

    Ticket addResolverToTicket(Long ticketId, String assignedUser);

    Domain findDomainWithId(Long id);

    Priority findPriorityByName(String name);

    TicketType findTicketTypeWithId(Long id);

    Topic findTopicWithId(Long id);

    Ticket setTicketStatus(Long ticketId, String ticketStatus);

    Ticket answerOnTicketWithId(Long ticketId, String answer);

    Iterable<Ticket> findTicketsForResolver(String username);

    User createUser(String username, String password);

    Iterable<User> findAllUsers();

    User findUserWithUsername(String username);

    Iterable<Domain> findAllDomains();

    Iterable<Priority> findAllPriorities();

    Iterable<Role> findAllRoles();

    Iterable<Source> findAllSources();

    Source findSourceWithId(Long id);

    Iterable<TicketType> findAllTicketTypes();

    Iterable<Topic> findAllTopics();

    Priority findPriorityWithId(Long id);

}