package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.AssignService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AssignControllerTest {

    @Mock
    private AssignService assignService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AssignController assignController;

    @Test
    public void assignUserToTicketTest() {
        Ticket ticket = new Ticket(1L);
        ticket.setAssignedGroup("Administrators");
        ticket.setAssignedUser("Kurt");
        when(assignService.addResolverToTicket(1L, "Kurt")).thenReturn(ticket);

        String askedAnswer = assignController.assignUserToTicket("1", "Kurt");
        JSONObject jsonAnswer = new JSONObject(askedAnswer);
        assertEquals("User has been added to ticket", jsonAnswer.getString("result"));
    }

    @Test
    public void assignFalseUserToTicketTest() {
        Ticket ticket = new Ticket(1L);
        ticket.setAssignedGroup("Administrators");
        ticket.setAssignedUser("Pieter");
        when(assignService.addResolverToTicket(1L, "Pieter")).thenReturn(null);

        String askedAnswer = assignController.assignUserToTicket("1", "Pieter");
        JSONObject jsonAnswer = new JSONObject(askedAnswer);
        assertEquals("User was not found", jsonAnswer.getString("result"));
    }

    @Test
    public void makeJsonObjectTest() {
        String answer = assignController.makeJsonObject("Man is greater than machine");
        JSONObject jsonAnswer = new JSONObject(answer);
        assertEquals("Man is greater than machine", jsonAnswer.getString("result"));
        assertNotNull(answer);

    }
}