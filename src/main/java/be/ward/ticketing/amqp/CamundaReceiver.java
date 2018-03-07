package be.ward.ticketing.amqp;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Constants;
import be.ward.ticketing.util.Messages;
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
public class CamundaReceiver {

    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private ProcessEngine processEngine;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_NEW_TICKET, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_NEW_TICKET))
    @Transactional
    void newMessageMade(String ticketId) {
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));

        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_TICKET_ID, ticket.getId());
        variables.put(Constants.VAR_CREATOR, ticket.getCreator());
        variables.put(Constants.VAR_CREATED_AT, ticket.getCreatedAt());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ticket", variables);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_TICKET_ANSWERED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_TICKET_ANSWERED))
    @Transactional
    void sendAnswerOnTicket(String ticketId) {
        processEngine.getRuntimeService().createMessageCorrelation(Messages.MSG_TICKET_ANSWERED)
                .processInstanceVariableEquals(Constants.VAR_TICKET_ID, Long.valueOf(ticketId))
                .correlateWithResult();
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_RESOLVER_ADDED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_RESOLVER_ADDED))
    @Transactional
    void resolverAssignedToTicket(String ticketId) {
        String resolver = ticketingService.findTicket(Long.valueOf(ticketId)).getAssignedUser();
        processEngine.getRuntimeService().createMessageCorrelation(Messages.MSG_RESOLVER_ADDED)
                .processInstanceVariableEquals(Constants.VAR_TICKET_ID, Long.valueOf(ticketId))
                .correlateWithResult();
    }
}
