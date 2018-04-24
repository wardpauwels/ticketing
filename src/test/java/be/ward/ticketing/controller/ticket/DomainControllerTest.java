package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Domain;
import be.ward.ticketing.service.ticket.DomainService;
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
public class DomainControllerTest {

    @Mock
    private DomainService domainService;

    @InjectMocks
    private DomainController domainController;

    @Test
    public void findAllDomainsTest() {
        Domain firstDomain = new Domain();
        firstDomain.setName("First domain");
        Domain secondDomain = new Domain();
        secondDomain.setName("The second domain");
        when(domainService.findAllDomains()).thenReturn(Lists.newArrayList(firstDomain, secondDomain));

        List<Domain> domains = domainController.findAllDomains();

        assertNotNull(domains);
        assertEquals(2, domains.size());
        assertEquals("First domain", firstDomain.getName());
        assertEquals("The second domain", secondDomain.getName());

        verify(domainService, times(1)).findAllDomains();
        verifyNoMoreInteractions(domainService);
    }
}