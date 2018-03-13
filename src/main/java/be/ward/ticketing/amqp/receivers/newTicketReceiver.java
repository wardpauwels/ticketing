package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Messages;
import be.ward.ticketing.util.TicketStatus;
import be.ward.ticketing.util.Variables;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class newTicketReceiver {

    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private ProcessEngine processEngine;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_NEW_TICKET, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_NEW_TICKET))
    @Transactional
    void sendMessageNewMessageMade(String ticketId) {
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));

        Map<String, Object> variables = new HashMap<>();
        variables.put(Variables.VAR_TICKET_ID, ticket.getId());
        variables.put(Variables.VAR_CREATOR, ticket.getCreator());
        variables.put(Variables.VAR_DESCRIPTION, ticket.getDescription());
        variables.put(Variables.VAR_CREATED_AT, ticket.getCreatedAt());
        variables.put(Variables.VAR_STATUS, TicketStatus.newTicket);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ticket", String.valueOf(ticket.getId()), variables);
    }

}
