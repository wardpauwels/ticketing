package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Source;
import be.ward.ticketing.service.ticket.SourceService;
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
public class SourceControllerTest {

    @Mock
    private SourceService sourceService;

    @InjectMocks
    private SourceController sourceController;

    @Test
    public void findAllSourcesTest() {
        Source firstSource = new Source();
        firstSource.setName("My first source.");
        Source secondSource = new Source();
        secondSource.setName("The second source.");
        when(sourceService.findAllSources()).thenReturn(Lists.newArrayList(firstSource, secondSource));

        List<Source> sources = sourceController.findAllSources();

        assertNotNull(sources);
        assertEquals(2, sources.size());
        assertEquals("My first source.", firstSource.getName());
        assertEquals("The second source.", secondSource.getName());

        verify(sourceService, times(1)).findAllSources();
        verifyNoMoreInteractions(sourceService);
    }
}