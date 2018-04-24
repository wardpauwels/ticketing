package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class SourceTest {

    @Test
    public void createAndReadSourceTest() {
        Source source = new Source();

        source.setLockVersion(1);
        source.setName("My source");

        assertNull(source.getId());
        assertEquals(1, (int) source.getLockVersion());
        assertEquals("My source", source.getName());
    }

}