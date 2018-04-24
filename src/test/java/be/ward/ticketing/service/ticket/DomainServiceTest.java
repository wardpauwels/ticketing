package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.DomainRepository;
import be.ward.ticketing.entities.ticketing.Domain;
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
public class DomainServiceTest {

    @Mock
    private DomainRepository domainRepository;

    @InjectMocks
    private DomainService domainService;

    private Domain firstDomain;
    private Domain secondDomain;

    @Before
    public void setUp() {
        firstDomain = new Domain(1L);
        firstDomain.setName("My first domain");
        secondDomain = new Domain(2L);
        secondDomain.setName("Second domain");
    }

    @Test
    public void findAllDomainsTest() {
        when(domainRepository.findAll()).thenReturn(Lists.newArrayList(firstDomain, secondDomain));

        List<Domain> domains = Lists.newArrayList(domainService.findAllDomains());

        assertNotNull(domains);
        assertEquals(2, domains.size());
        assertEquals(firstDomain.getName(), domains.get(0).getName());
        assertEquals(secondDomain.getName(), domains.get(1).getName());
        verify(domainRepository, times(1)).findAll();
        verifyNoMoreInteractions(domainRepository);
    }

    @Test
    public void findDomainWithIdTest() {
        when(domainRepository.findOne(1L)).thenReturn(firstDomain);

        Domain askedDomain = domainService.findDomainWithId(1L);

        assertNotNull(askedDomain);
        assertEquals(firstDomain.getId(), askedDomain.getId());
        assertEquals(firstDomain.getName(), askedDomain.getName());

        verify(domainRepository, times(1)).findOne(1L);
        verifyNoMoreInteractions(domainRepository);
    }
}