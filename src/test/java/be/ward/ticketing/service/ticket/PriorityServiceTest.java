package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.PriorityRepository;
import be.ward.ticketing.entities.ticketing.Priority;
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
public class PriorityServiceTest {

    @Mock
    private PriorityRepository priorityRepository;

    @InjectMocks
    private PriorityService priorityService;

    private Priority firstPriority;
    private Priority secondPriority;

    @Before
    public void setUp() {
        firstPriority = new Priority(1L);
        firstPriority.setName("My first priority");
        secondPriority = new Priority(2L);
        secondPriority.setName("Second priority");
    }

    @Test
    public void findAllPrioritiesTest() {
        when(priorityRepository.findAll()).thenReturn(Lists.newArrayList(firstPriority, secondPriority));

        List<Priority> priorities = Lists.newArrayList(priorityService.findAllPriorities());

        assertNotNull(priorities);
        assertEquals(2, priorities.size());
        assertEquals(firstPriority.getName(), priorities.get(0).getName());
        assertEquals(secondPriority.getName(), priorities.get(1).getName());
        verify(priorityRepository, times(1)).findAll();
        verifyNoMoreInteractions(priorityRepository);
    }

    @Test
    public void findPriorityWithIdTest() {
        when(priorityRepository.findOne(1L)).thenReturn(firstPriority);

        Priority askedPriority = priorityService.findPriorityWithId(1L);

        assertNotNull(askedPriority);
        assertEquals(firstPriority.getId(), askedPriority.getId());
        assertEquals(firstPriority.getName(), askedPriority.getName());

        verify(priorityRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(priorityRepository);
    }

    @Test
    public void findPriorityByName() {
        when(priorityRepository.findByNameEquals("Second priority")).thenReturn(secondPriority);

        Priority askedPriority = priorityService.findPriorityByName("Second priority");

        assertNotNull(askedPriority);
        assertEquals(secondPriority.getId(), askedPriority.getId());
        assertEquals(secondPriority.getName(), askedPriority.getName());

        verify(priorityRepository, times(1)).findByNameEquals("Second priority");
        verifyNoMoreInteractions(priorityRepository);
    }
}