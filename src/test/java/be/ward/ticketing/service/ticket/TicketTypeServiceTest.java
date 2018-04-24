package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TicketTypeRepository;
import be.ward.ticketing.entities.ticketing.TicketType;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Before;
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
public class TicketTypeServiceTest {

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @InjectMocks
    private TicketTypeService ticketTypeService;

    private TicketType firstTicketType;
    private TicketType secondTicketType;

    @Before
    public void setUp() {
        firstTicketType = new TicketType(1L);
        firstTicketType.setName("My first ticket type");
        secondTicketType = new TicketType(2L);
        secondTicketType.setName("Second ticket type");
    }

    @Test
    public void findAllTicketTypesTest() {
        when(ticketTypeRepository.findAll()).thenReturn(Lists.newArrayList(firstTicketType, secondTicketType));

        List<TicketType> ticketTypes = Lists.newArrayList(ticketTypeService.findAllTicketTypes());

        assertNotNull(ticketTypes);
        assertEquals(2, ticketTypes.size());
        assertEquals(firstTicketType.getName(), ticketTypes.get(0).getName());
        assertEquals(secondTicketType.getName(), ticketTypes.get(1).getName());
        verify(ticketTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(ticketTypeRepository);
    }

    @Test
    public void findTicketTypeWithIdTest() {
        when(ticketTypeRepository.findOne(1L)).thenReturn(firstTicketType);

        TicketType askedTicketType = ticketTypeService.findTicketTypeWithId(1L);

        assertNotNull(askedTicketType);
        assertEquals(firstTicketType.getId(), askedTicketType.getId());
        assertEquals(firstTicketType.getName(), askedTicketType.getName());
        verify(ticketTypeRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(ticketTypeRepository);
    }
}