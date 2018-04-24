package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TicketRepository;
import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TicketService {

    private final AssociationService associationService;
    private final DomainService domainService;
    private final PriorityService priorityService;
    private final SourceService sourceService;
    private final TicketTypeService ticketTypeService;
    private final TopicService topicService;

    private final TicketRepository ticketRepository;

    @Autowired
    public TicketService(AssociationService associationService,
                         DomainService domainService,
                         PriorityService priorityService,
                         SourceService sourceService,
                         TicketTypeService ticketTypeService,
                         TopicService topicService,
                         TicketRepository ticketRepository) {
        this.associationService = associationService;
        this.domainService = domainService;
        this.priorityService = priorityService;
        this.sourceService = sourceService;
        this.ticketTypeService = ticketTypeService;
        this.topicService = topicService;
        this.ticketRepository = ticketRepository;
    }

    public Ticket createTicket(String username, String message) {
        Ticket ticket = new Ticket();

        ticket.setCreator(username);
        ticket.setDescription(message);
        ticket.setCreatedAt(new Date());
        ticket.setStatus(TicketStatus.newTicket);

        ticket.setDomain(domainService.findDomainWithId(1L));
        ticket.setPriority(priorityService.findPriorityWithId(1L));
        ticket.setSource(sourceService.findSourceWithId(1L));
        ticket.setTicketType(ticketTypeService.findTicketTypeWithId(1L));
        ticket.setTopic(topicService.findTopicWithId(1L));

        return ticketRepository.save(ticket);
    }

    public Ticket findTicket(Long id) {
        return ticketRepository.findOne(id);
    }

    public Iterable<Ticket> findTicketsWithoutResolver() {
        return ticketRepository.findByAssignedUserIsNull();
    }

    public Iterable<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    private void setTicketStatus(Long ticketId, String ticketStatus) {
        Ticket ticket = findTicket(ticketId);
        ticket.setStatus(ticketStatus);
        ticketRepository.save(ticket);
    }

    public Ticket answerOnTicketWithId(Long ticketId, String user, String answer) {
        Ticket ticket = findTicket(ticketId);
        setTicketStatus(ticket.getId(), TicketStatus.ticketAnswered);
        Association association = associationService.getLastAssociationFromTicket(ticket);

        Ticket answerTicket = createTicket(user, answer);
        Association answerAssociation = associationService.createAssociation(AssociationTypes.answer, answerTicket);

        associationService.linkNewAssociationToAssociation(answerAssociation, association);

        return ticketRepository.save(answerTicket);
    }

    public Iterable<Ticket> findTicketsForResolver(String username) {
        return ticketRepository.findByAssignedUser(username);
    }
}