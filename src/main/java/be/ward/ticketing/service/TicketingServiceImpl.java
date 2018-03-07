package be.ward.ticketing.service;

import be.ward.ticketing.data.*;
import be.ward.ticketing.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketingServiceImpl implements TicketingService {

    private AssociationDao associationDao;
    private DomainDao domainDao;
    private PriorityDao priorityDao;
    private SourceDao sourceDao;
    private TicketDao ticketDao;
    private TicketTypeDao ticketTypeDao;
    private TopicDao topicDao;

    @Autowired
    public void setAssociationDao(AssociationDao associationDao) {
        this.associationDao = associationDao;
    }

    @Autowired
    public void setDomainDao(DomainDao domainDao) {
        this.domainDao = domainDao;
    }

    @Autowired
    public void setPriorityDao(PriorityDao priorityDao) {
        this.priorityDao = priorityDao;
    }

    @Autowired
    public void setSourceDao(SourceDao sourceDao) {
        this.sourceDao = sourceDao;
    }

    @Autowired
    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    @Autowired
    public void setTicketTypeDao(TicketTypeDao ticketTypeDao) {
        this.ticketTypeDao = ticketTypeDao;
    }

    @Autowired
    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    // TICKETS
    @Override
    public Ticket createTicket(String username, String message) {
        Ticket ticket = new Ticket();

        ticket.setCreator(username);
        ticket.setDescription(message);
        ticket.setCreatedAt(new Date());
        ticket.setStatus("Unresolved");

        ticket.setDomain(domainDao.findOne(1L));
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
    public Ticket addResolverToTicket(String ticketId, String assignedUser) {
        Ticket ticket = findTicket(Long.valueOf(ticketId));
        ticket.setAssignedUser(assignedUser);
        return ticketDao.save(ticket);
    }

    //DOMAIN

    @Override
    public Domain findDomainWithId(long id) {
        return domainDao.findOne(id);
    }


    //SOURCE

    @Override
    public Source findSourceWithId(long id) {
        return sourceDao.findOne(id);
    }

    //TICKET TYPE

    @Override
    public TicketType findTicketTypeWithId(long id) {
        return ticketTypeDao.findOne(id);
    }

    //TOPIC

    @Override
    public Topic findTopicWithId(long id) {
        return topicDao.findOne(id);
    }

}
