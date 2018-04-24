package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class DomainTest {

    @Test
    public void createAndReadDomainTest() {
        Domain domain = new Domain();

        domain.setLockVersion(1);
        domain.setName("My domain");

        assertNull(domain.getId());
        assertEquals(1, (int) domain.getLockVersion());
        assertEquals("My domain", domain.getName());
    }

}