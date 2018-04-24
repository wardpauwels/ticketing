package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.AssociationService;
import be.ward.ticketing.service.ticket.TicketService;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.TicketStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TicketControllerTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private AssociationService associationService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TicketController ticketController;

    private Ticket firstTicket;
    private Ticket secondTicket;
    private Ticket thirdTicket;
    private Association firstAssociation;

    @Before
    public void setUp() {
        Date date = new Date();

        firstTicket = new Ticket(5L);
        firstTicket.setStatus(TicketStatus.ticketClosed);
        firstTicket.setCreator("Bert");
        firstTicket.setAssignedUser("Bram");
        firstTicket.setDescription("Mijn lampen werken niet.");

        firstAssociation = new Association(1L, AssociationTypes.ticket, firstTicket);

        secondTicket = new Ticket(9L);
        secondTicket.setStatus(TicketStatus.newTicket);
        secondTicket.setCreator("Flor");
        secondTicket.setAssignedUser("Bram");
        secondTicket.setCreatedAt(date);
        secondTicket.setDescription("Steek de stekker in.");

        Association secondAssociation = new Association(2L, AssociationTypes.answer, secondTicket);
        firstAssociation.setAssociation(secondAssociation);

        thirdTicket = new Ticket();
    }

    @Test
    public void getTicketWithIdTest() {
        when(ticketService.findTicket(5L)).thenReturn(firstTicket);

        Ticket askedTicket = ticketController.findTicketWithId("5");

        assertEquals(firstTicket.getClass(), askedTicket.getClass());
        assertEquals(firstTicket.getId(), askedTicket.getId());
        assertEquals(firstTicket.getStatus(), askedTicket.getStatus());
        verify(ticketService, times(1)).findTicket(5L);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void getAllTicketsTest() {
        List<Ticket> ticketList = Arrays.asList(firstTicket, secondTicket);

        when(ticketService.findAllTickets()).thenReturn(ticketList);

        List<Ticket> tickets = ticketController.findAllTickets();

        assertNotNull(tickets);
        assertEquals(2, tickets.size());
        verify(ticketService, times(1)).findAllTickets();
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void getAllTicketsWithoutResolverTest() {
        List<Ticket> ticketList = Arrays.asList(firstTicket, secondTicket, thirdTicket);

        when(ticketService.findTicketsWithoutResolver()).thenReturn(ticketList);

        List<Ticket> tickets = ticketController.findTicketsWithoutResolver();

        assertNotNull(tickets);
        assertNull(tickets.get(2).getAssignedUser());
        verify(ticketService, times(1)).findTicketsWithoutResolver();
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void getAllTicketsForUserTest() {
        List<Ticket> ticketList = Arrays.asList(firstTicket, secondTicket);

        when(ticketService.findTicketsForResolver("Bram")).thenReturn(ticketList);

        List<Ticket> tickets = ticketController.getTicketForResolver("Bram");

        assertNotNull(tickets);
        assertEquals("Bram", tickets.get(0).getAssignedUser());
        assertEquals("Bram", tickets.get(1).getAssignedUser());

        verify(ticketService, times(1)).findTicketsForResolver("Bram");
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void addNewTicketTest() {
        when(ticketService.createTicket("Bert", "Mijn lampen werken niet.")).thenReturn(firstTicket);
        when(associationService.createAssociation(AssociationTypes.ticket, firstTicket)).thenReturn(firstAssociation);

        Ticket addedTicket = ticketController.createNewTicket("Bert", "Mijn lampen werken niet.");

        assertNotNull(addedTicket);
        assertEquals(firstTicket.getCreator(), addedTicket.getCreator());
        assertEquals(firstTicket.getStatus(), addedTicket.getStatus());
        assertEquals(firstTicket.getDescription(), addedTicket.getDescription());
        assertEquals(firstTicket.getCreatedAt(), addedTicket.getCreatedAt());

        verify(ticketService, times(1)).createTicket("Bert", "Mijn lampen werken niet.");
        verify(associationService, times(1)).createAssociation(AssociationTypes.ticket, firstTicket);
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void answerOnTicketWithIdTest() {
        when(ticketService.findTicket(5L)).thenReturn(firstTicket);
        when(associationService.getLastAssociationFromTicket(firstTicket)).thenReturn(firstAssociation);
        when(ticketService.answerOnTicketWithId(firstAssociation.getTicket().getId(), "Flor", "Steek de stekker in.")).thenReturn(secondTicket);

        Ticket ticket = ticketController.answerOnTicketWithId(String.valueOf(firstTicket.getId()), "Flor", "Steek de stekker in.");

        assertNotNull(ticket);

        verify(ticketService, times(1)).findTicket(5L);
        verify(associationService, times(1)).getLastAssociationFromTicket(firstTicket);
        verify(ticketService, times(1)).answerOnTicketWithId(firstAssociation.getTicket().getId(), "Flor", "Steek de stekker in.");
        verifyNoMoreInteractions(ticketService);
    }

    @Test
    public void ticketSolvedTest() {
        when(ticketService.findTicket(secondTicket.getId())).thenReturn(secondTicket);
        when(associationService.getTopAssociationFromTicket(secondTicket)).thenReturn(firstAssociation);
        Ticket ticket = ticketController.ticketSolved(String.valueOf(secondTicket.getId()));

        assertNotNull(ticket);
        assertEquals(firstTicket.getId(), ticket.getId());
        verify(associationService, times(1)).getTopAssociationFromTicket(secondTicket);
        verifyNoMoreInteractions(associationService);
    }

    @Test
    public void ticketNotSolvedTest() {
        when(ticketService.findTicket(secondTicket.getId())).thenReturn(secondTicket);
        when(associationService.getTopAssociationFromTicket(secondTicket)).thenReturn(firstAssociation);
        Ticket ticket = ticketController.ticketNotSolved(String.valueOf(secondTicket.getId()), "De stekker steekt in.");

        assertNotNull(ticket);
        assertEquals(firstTicket.getId(), ticket.getId());
        verify(associationService, times(1)).getTopAssociationFromTicket(secondTicket);
        verifyNoMoreInteractions(associationService);
    }

}