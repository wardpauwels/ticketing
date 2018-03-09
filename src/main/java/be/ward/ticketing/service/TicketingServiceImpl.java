package be.ward.ticketing.service;

import be.ward.ticketing.data.*;
import be.ward.ticketing.entities.*;
import be.ward.ticketing.util.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketingServiceImpl implements TicketingService {

    @Autowired
    private AssociationDao associationDao;
    @Autowired
    private DomainDao domainDao;
    @Autowired
    private PriorityDao priorityDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private SourceDao sourceDao;
    @Autowired
    private TicketDao ticketDao;
    @Autowired
    private TicketTypeDao ticketTypeDao;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private UserDao userDao;

    // TICKETS
    @Override
    public Ticket createTicket(String username, String message) {
        Ticket ticket = new Ticket();

        ticket.setCreator(username);
        ticket.setDescription(message);
        ticket.setCreatedAt(new Date());
        ticket.setStatus(TicketStatus.newTicket);

        ticket.setDomain(domainDao.findOne(1L));
        ticket.setPriority(priorityDao.findOne(1L));
        ticket.setSource(sourceDao.findOne(1L));
        ticket.setTicketType(ticketTypeDao.findOne(1L));
        ticket.setTopic(topicDao.findOne(1L));

        return ticketDao.save(ticket);
    }

    @Override
    public Ticket findTicket(Long id) {
        return ticketDao.findOne(id);
    }

    @Override
    public Iterable<Ticket> findTicketsWithoutResolver() {
        return ticketDao.findByAssignedUserIsNull();
    }

    @Override
    public Iterable<Ticket> findAllTickets() {
        return ticketDao.findAll();
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketDao.save(ticket);
    }

    @Override
    public Ticket addResolverToTicket(Long ticketId, String assignedUser) {
        Ticket ticket = findTicket(ticketId);
        ticket.setAssignedUser(assignedUser);
        return ticketDao.save(ticket);
    }

    @Override
    public Iterable<Ticket> findTicketsForResolver(String username) {
        return ticketDao.findByAssignedUser(username);
    }

    //DOMAIN

    @Override
    public Domain findDomainWithId(Long id) {
        return domainDao.findOne(id);
    }

    //PRIORITY

    @Override
    public Priority findPriorityByName(String name) {
        return priorityDao.findByNameEquals(name);
    }

    //SOURCE

    @Override
    public Source findSourceWithId(Long id) {
        return sourceDao.findOne(id);
    }

    //TICKET TYPE

    @Override
    public TicketType findTicketTypeWithId(Long id) {
        return ticketTypeDao.findOne(id);
    }

    //TOPIC

    @Override
    public Topic findTopicWithId(Long id) {
        return topicDao.findOne(id);
    }

    // USER

    @Override
    public User createUser(String username, String password) {
        User user = new User(username, password);
        return userDao.save(user);
    }

    @Override
    public Iterable<User> findAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User findUserWithUsername(String username) {
        return userDao.findUserByUsername(username);
    }
}
