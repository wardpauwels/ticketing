package be.ward.ticketing.service;

import be.ward.ticketing.data.ticketing.*;
import be.ward.ticketing.data.user.RoleDao;
import be.ward.ticketing.data.user.UserDao;
import be.ward.ticketing.entities.ticketing.*;
import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketingServiceImpl implements TicketingService {

    @Autowired
    private AssociationDao associationDao;
    @Autowired
    private DomainDao domainDao;
    @Autowired
    private PasswordEncoder encoder;
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

    // ASSOCIATION
    @Override
    public Association createAssociation(String associationType, Ticket ticket) {
        Association association = new Association(associationType, ticket);
        return associationDao.save(association);
    }

    @Override
    public Association linkNewAssociationToAssociation(Association newAssociation, Association oldAssociation) {
        Association association = findAssociationById(oldAssociation.getId());
        association.setAssociation(newAssociation);
        return associationDao.save(association);
    }

    @Override
    public Association getTopAssociationFromTicket(Ticket ticket) {
        Association association = findAssociationByTicket(ticket);
        Association aboveAssociation = findAssociationByAssociation(association);
        while (aboveAssociation != null) {
            association = aboveAssociation;
            aboveAssociation = findAssociationByAssociation(association);
            if (aboveAssociation == null) return association;
        }
        return association;
    }

    @Override
    public Association getLastAssociationFromTicket(Ticket ticket) {
        Association association = findAssociationByTicket(ticket);
        while (association.getAssociation() != null) {
            association = association.getAssociation();
        }
        return association;
    }

    @Override
    public Association findAssociationById(Long associationId) {
        return associationDao.findOne(associationId);
    }

    @Override
    public Association findAssociationByTicket(Ticket ticket) {
        return associationDao.findByTicket(ticket);
    }

    @Override
    public Association findAssociationByAssociation(Association association) {
        return associationDao.findByAssociation(association);
    }

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
    public void saveTicket(Ticket ticket) {
        ticketDao.save(ticket);
    }

    @Override
    public Ticket addResolverToTicket(Long ticketId, String username) {
        Ticket ticket = findTicket(ticketId);
        User user = userDao.findUserByUsername(username);

        if (user != null) {
            ticket.setAssignedUser(username);
            return ticketDao.save(ticket);
        } else return null;
    }

    @Override
    public Ticket setTicketStatus(Long ticketId, String ticketStatus) {
        Ticket ticket = findTicket(ticketId);
        ticket.setStatus(ticketStatus);
        return ticketDao.save(ticket);
    }

    @Override
    public Ticket answerOnTicketWithId(Long ticketId, String user, String answer) {
        Ticket ticket = findTicket(ticketId);
        setTicketStatus(ticket.getId(), TicketStatus.ticketAnswered);
        Association association = getLastAssociationFromTicket(ticket);

        Ticket answerTicket = createTicket(user, answer);
        Association answerAssociation = createAssociation(AssociationTypes.answer, answerTicket);

        linkNewAssociationToAssociation(answerAssociation, association);

        return ticketDao.save(answerTicket);
    }

    @Override
    public Iterable<Ticket> findTicketsForResolver(String username) {
        return ticketDao.findByAssignedUser(username);
    }

    //DOMAIN
    @Override
    public Iterable<Domain> findAllDomains() {
        return domainDao.findAll();
    }

    @Override
    public Domain findDomainWithId(Long id) {
        return domainDao.findOne(id);
    }


    //PRIORITY
    @Override
    public Iterable<Priority> findAllPriorities() {
        return priorityDao.findAll();
    }

    @Override
    public Priority findPriorityWithId(Long id) {
        return priorityDao.findOne(id);
    }

    @Override
    public Priority findPriorityByName(String name) {
        return priorityDao.findByNameEquals(name);
    }


    // ROLE
    @Override
    public Iterable<Role> findAllRoles() {
        return roleDao.findAll();
    }


    //SOURCE
    @Override
    public Iterable<Source> findAllSources() {
        return sourceDao.findAll();
    }

    @Override
    public Source findSourceWithId(Long id) {
        return sourceDao.findOne(id);
    }

    //TICKET TYPE
    @Override
    public Iterable<TicketType> findAllTicketTypes() {
        return ticketTypeDao.findAll();
    }

    @Override
    public TicketType findTicketTypeWithId(Long id) {
        return ticketTypeDao.findOne(id);
    }


    //TOPIC
    @Override
    public Iterable<Topic> findAllTopics() {
        return topicDao.findAll();
    }

    @Override
    public Topic findTopicWithId(Long id) {
        return topicDao.findOne(id);
    }


    // USER
    @Override
    public User createUser(String username, String password) {
        User user = new User(username, encoder.encode(password));
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