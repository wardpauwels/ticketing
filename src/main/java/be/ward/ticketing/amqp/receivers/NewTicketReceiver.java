package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.AssociationService;
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

    private final ProcessEngine processEngine;
    private final AssociationService associationService;

    @Autowired
    public NewTicketReceiver(ProcessEngine processEngine, AssociationService associationService) {
        this.processEngine = processEngine;
        this.associationService = associationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_NEW_TICKET, durable = "true"),
            exchange = @Exchange(value = Constants.VAR_DEFAULT_EXCHANGE, type = "topic", durable = "true"),
            key = Messages.MSG_NEW_TICKET))
    @Transactional
    void sendMessageNewMessageMade(String associationId) {
        Association association = associationService.findAssociationById(Long.valueOf(associationId));
        Ticket ticket = association.getTicket();

        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_ASSOCIATION_ID, association.getId());
        variables.put(Constants.VAR_TICKET_ID, ticket.getId());
        variables.put(Constants.VAR_CREATOR, ticket.getCreator());
        variables.put(Constants.VAR_CREATED_AT, ticket.getCreatedAt());

        processEngine
                .getRuntimeService()
                .startProcessInstanceByKey("newTicket", String.valueOf(ticket.getId()), variables);
    }
}