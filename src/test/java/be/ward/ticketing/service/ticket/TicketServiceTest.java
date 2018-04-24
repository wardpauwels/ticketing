package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TicketRepository;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.util.ticket.TicketStatus;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketService ticketService;

    private Ticket firstTicket;
    private Ticket secondTicket;
    private Ticket thirdTicket;

    @Before
    public void setUp() {
        firstTicket = new Ticket(5L);
        firstTicket.setStatus(TicketStatus.ticketClosed);
        firstTicket.setCreator("Bert");
        firstTicket.setAssignedUser("Bram");
        firstTicket.setDescription("Mijn lampen werken niet.");

        secondTicket = new Ticket(9L);
        secondTicket.setStatus(TicketStatus.newTicket);
        secondTicket.setCreator("Flor");
        secondTicket.setAssignedUser("Bram");
        secondTicket.setDescription("Steek de stekker in.");

        thirdTicket = new Ticket();
    }

    @Test
    public void findTicketWithIdTest() {
        when(ticketRepository.findOne(5L)).thenReturn(firstTicket);
        Ticket ticket = ticketService.findTicket(5L);

        assertNotNull(ticket);
        assertEquals((Long) 5L, ticket.getId());
    }

    @Test
    public void findTicketsWithoutResolverTest() {
        when(ticketRepository.findByAssignedUserIsNull()).thenReturn(Lists.newArrayList(thirdTicket));
        List<Ticket> ticketsWithoutResolver = Lists.newArrayList(ticketService.findTicketsWithoutResolver());

        for (Ticket ticket : ticketsWithoutResolver) {
            assertNull(ticket.getAssignedUser());
        }
    }

    @Test
    public void getAllTicketsTest() {
        when(ticketRepository.findAll()).thenReturn(Lists.newArrayList(firstTicket, secondTicket));

        List<Ticket> tickets = Lists.newArrayList(ticketService.findAllTickets());

        assertNotNull(tickets);
        assertEquals(tickets.get(0).getClass(), Ticket.class);
        verify(ticketRepository, times(1)).findAll();
        verifyNoMoreInteractions(ticketRepository);
    }

    @Test
    public void setTicketStatusTest() {

    }

    @Test
    public void answerOnTicketWithIdTest() {

    }

    @Test
    public void findTicketsForResolverTest() {
        when(ticketRepository.findByAssignedUser(firstTicket.getAssignedUser()))
                .thenReturn(Collections.singletonList(firstTicket));
        List<Ticket> tickets = Lists.newArrayList(ticketService.findTicketsForResolver(firstTicket.getAssignedUser()));

        assertNotNull(tickets);
        assertEquals(firstTicket.getId(), tickets.get(0).getId());
        verify(ticketRepository, times(1)).findByAssignedUser(firstTicket.getAssignedUser());
        verifyNoMoreInteractions(ticketRepository);
    }
}