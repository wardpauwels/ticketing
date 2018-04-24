package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.SourceRepository;
import be.ward.ticketing.entities.ticketing.Source;
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
public class SourceServiceTest {

    @Mock
    private SourceRepository sourceRepository;

    @InjectMocks
    private SourceService sourceService;

    private Source firstSource;
    private Source secondSource;

    @Before
    public void setUp() {
        firstSource = new Source(1L);
        firstSource.setName("My first source");
        secondSource = new Source(2L);
        secondSource.setName("Second source");
    }

    @Test
    public void findAllSourcesTest() {
        when(sourceRepository.findAll()).thenReturn(Lists.newArrayList(firstSource, secondSource));

        List<Source> sources = Lists.newArrayList(sourceService.findAllSources());

        assertNotNull(sources);
        assertEquals(2, sources.size());
        assertEquals(firstSource.getName(), sources.get(0).getName());
        assertEquals(secondSource.getName(), sources.get(1).getName());
        verify(sourceRepository, times(1)).findAll();
        verifyNoMoreInteractions(sourceRepository);
    }

    @Test
    public void findSourceWithIdTest() {
        when(sourceRepository.findOne(1L)).thenReturn(firstSource);

        Source askedSource = sourceService.findSourceWithId(1L);

        assertNotNull(askedSource);
        assertEquals(firstSource.getId(), askedSource.getId());
        assertEquals(firstSource.getName(), askedSource.getName());
        verify(sourceRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(sourceRepository);
    }
}