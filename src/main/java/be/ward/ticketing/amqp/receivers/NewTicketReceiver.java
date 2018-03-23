package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import org.camunda.bpm.engine.ProcessEngine;
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
public class NewTicketReceiver {

    private final TicketingService ticketingService;
    private final ProcessEngine processEngine;

    @Autowired
    public NewTicketReceiver(TicketingService ticketingService, ProcessEngine processEngine) {
        this.ticketingService = ticketingService;
        this.processEngine = processEngine;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_NEW_TICKET, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_NEW_TICKET))
    @Transactional
    void sendMessageNewMessageMade(String associationId) {
        Association association = ticketingService.findAssociationById(Long.valueOf(associationId));
        Ticket ticket = association.getTicket();

        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_ASSOCIATION_ID, association.getId());
        variables.put(Constants.VAR_TICKET_ID, ticket.getId());
        variables.put(Constants.VAR_CREATOR, ticket.getCreator());
        variables.put(Constants.VAR_CREATED_AT, ticket.getCreatedAt());

        processEngine
                .getRuntimeService()
                .startProcessInstanceByKey("ticket", String.valueOf(ticket.getId()), variables);
    }
}