package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class PriorityTest {

    @Test
    public void createAndReadPriorityTest() {
        Priority priority = new Priority();

        priority.setLockVersion(1);
        priority.setName("My priority");

        assertNull(priority.getId());
        assertEquals(1, (int) priority.getLockVersion());
        assertEquals("My priority", priority.getName());
    }

}