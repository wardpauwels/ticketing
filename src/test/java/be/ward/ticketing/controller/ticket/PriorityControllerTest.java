package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Priority;
import be.ward.ticketing.service.ticket.PriorityService;
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
public class PriorityControllerTest {

    @Mock
    private PriorityService priorityService;

    @InjectMocks
    private PriorityController priorityController;

    @Test
    public void findAllPrioritiesTest() {
        Priority firstPriority = new Priority();
        firstPriority.setName("First priority");
        Priority secondPriority = new Priority();
        secondPriority.setName("The second priority");
        when(priorityService.findAllPriorities()).thenReturn(Lists.newArrayList(firstPriority, secondPriority));

        List<Priority> priorities = priorityController.findAllPriorities();

        assertNotNull(priorities);
        assertEquals(2, priorities.size());
        assertEquals("First priority", firstPriority.getName());
        assertEquals("The second priority", secondPriority.getName());

        verify(priorityService, times(1)).findAllPriorities();
        verifyNoMoreInteractions(priorityService);
    }
}