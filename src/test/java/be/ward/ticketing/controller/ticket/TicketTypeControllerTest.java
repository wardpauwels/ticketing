package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.TicketType;
import be.ward.ticketing.service.ticket.TicketTypeService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TicketTypeControllerTest {

    @Mock
    private TicketTypeService ticketTypeService;

    @InjectMocks
    private TicketTypeController ticketTypeController;

    @Test
    public void findAllTicketTypesTest() {
        TicketType typeOne = new TicketType();
        TicketType typeTwo = new TicketType();
        when(ticketTypeService.findAllTicketTypes()).thenReturn(Lists.newArrayList(typeOne, typeTwo));

        List<TicketType> ticketTypes = ticketTypeController.findAllTicketTypes();

        assertNotNull(ticketTypes);
        assertEquals(2, ticketTypes.size());
        assertEquals(typeOne.getName(), ticketTypes.get(0).getName());
        assertEquals(typeTwo.getName(), ticketTypes.get(1).getName());
        verify(ticketTypeService, times(1)).findAllTicketTypes();
        verifyNoMoreInteractions(ticketTypeService);
    }
}