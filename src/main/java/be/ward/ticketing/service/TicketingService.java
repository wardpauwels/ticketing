package be.ward.ticketing.service;

import be.ward.ticketing.entities.ticketing.*;
import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.entities.user.User;

public interface TicketingService {

    Association findAssociationByTicket(Ticket ticket);

    Association findAssociationByAssociation(Association association);

    Ticket createTicket(String username, String message);

    Ticket findTicket(Long id);

    Iterable<Ticket> findTicketsWithoutResolver();

    Iterable<Ticket> findAllTickets();

    void saveTicket(Ticket ticket);

    Ticket addResolverToTicket(Long ticketId, String assignedUser);

    Domain findDomainWithId(Long id);

    Priority findPriorityByName(String name);

    Topic findTopicWithId(Long id);

    Ticket setTicketStatus(Long ticketId, String ticketStatus);

    Ticket answerOnTicketWithId(Long ticketId, String user, String answer);

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

    TicketType findTicketTypeWithId(Long id);

    Iterable<Topic> findAllTopics();

    Priority findPriorityWithId(Long id);

    Association createAssociation(String associationType, Ticket ticket);

    Association linkNewAssociationToAssociation(Association newAssociation, Association oldAssociation);

    Association getTopAssociationFromTicket(Ticket ticket);

    Association getLastAssociationFromTicket(Ticket ticket);

    Association findAssociationById(Long associationId);
}