package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class TicketTypeTest {

    @Test
    public void createAndReadTicketTypeTest() {
        TicketType ticketType = new TicketType();

        ticketType.setLockVersion(1);
        ticketType.setName("My ticket type");

        assertNull(ticketType.getId());
        assertEquals(1, (int) ticketType.getLockVersion());
        assertEquals("My ticket type", ticketType.getName());
    }

}