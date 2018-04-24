package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class TopicTest {

    @Test
    public void createAndReadTopicTest() {
        Topic topic = new Topic();

        topic.setLockVersion(1);
        topic.setName("My topic");

        assertNull(topic.getId());
        assertEquals(1, (int) topic.getLockVersion());
        assertEquals("My topic", topic.getName());
    }

}