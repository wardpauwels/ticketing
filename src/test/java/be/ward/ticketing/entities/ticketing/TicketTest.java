package be.ward.ticketing.entities.ticketing;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class TicketTest {

    @Test
    public void createAndReadTicketTest() {
        Ticket ticket = new Ticket();
        Date date = new Date();
        Topic topic = new Topic();
        Domain domain = new Domain();
        Priority priority = new Priority();
        Source source = new Source();
        TicketType ticketType = new TicketType();

        //set all values
        ticket.setAssignedGroup("Moderators");
        ticket.setAssignedUser("Ward");
        ticket.setCreatedAt(date);
        ticket.setCreator("Bertje");
        ticket.setDescription("Ik heb een probleem met de verwarming");
        ticket.setDueAt(date);
        ticket.setLockVersion(1);
        ticket.setStatus("New ticket");
        ticket.setTopic(topic);
        ticket.setTopicText("My topic");
        ticket.setDomain(domain);
        ticket.setPriority(priority);
        ticket.setSource(source);
        ticket.setTicketType(ticketType);

        assertNull(ticket.getId());
        assertEquals("Moderators", ticket.getAssignedGroup());
        assertEquals("Ward", ticket.getAssignedUser());
        assertEquals(date, ticket.getCreatedAt());
        assertEquals("Bertje", ticket.getCreator());
        assertEquals("Ik heb een probleem met de verwarming", ticket.getDescription());
        assertEquals(date, ticket.getDueAt());
        assertEquals(1, (int) ticket.getLockVersion());
        assertEquals("New ticket", ticket.getStatus());
        assertEquals(topic, ticket.getTopic());
        assertEquals("My topic", ticket.getTopicText());
        assertEquals(domain, ticket.getDomain());
        assertEquals(priority, ticket.getPriority());
        assertEquals(source, ticket.getSource());
        assertEquals(ticketType, ticket.getTicketType());
    }

}