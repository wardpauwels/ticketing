package be.ward.ticketing.service.ticket;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.service.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AssignServiceTest {

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AssignService assignService;

    private User user;
    private Ticket ticket;

    @Before
    public void setUp() {
        user = new User("ward", "pauwels");
        ticket = new Ticket(1L);
        ticket.setAssignedGroup("Moderator");
    }

    @Test
    public void addResolverToTicket() {
        when(ticketService.findTicket(1L)).thenReturn(ticket);
        when(userService.findUserWithUsername("ward")).thenReturn(user);
        when(ticketService.saveTicket(ticket)).thenReturn(ticket);

        Ticket askedTicket = assignService.addResolverToTicket(1L, "ward");

        assertNotNull(askedTicket);
        verify(ticketService, times(1)).findTicket(1L);
        verify(ticketService, times(1)).saveTicket(ticket);
        verify(userService, times(1)).findUserWithUsername("ward");
        verifyNoMoreInteractions(ticketService, userService);
    }

    @Test
    public void addFalseResolverToTicket() {
        when(ticketService.findTicket(1L)).thenReturn(ticket);
        when(userService.findUserWithUsername("bram")).thenReturn(null);

        Ticket askedTicket = assignService.addResolverToTicket(1L, "bram");

        assertNull(askedTicket);
        verify(ticketService, times(1)).findTicket(1L);
        verify(userService, times(1)).findUserWithUsername("bram");
        verifyNoMoreInteractions(ticketService, userService);
    }
}