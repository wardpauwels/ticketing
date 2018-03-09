package be.ward.ticketing.amqp;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Messages;
import be.ward.ticketing.util.TicketStatus;
import be.ward.ticketing.util.Variables;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.task.Task;
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
    void sendMessageNewMessageMade(String ticketId) {
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));

        Map<String, Object> variables = new HashMap<>();
        variables.put(Variables.VAR_TICKET_ID, ticket.getId());
        variables.put(Variables.VAR_CREATOR, ticket.getCreator());
        variables.put(Variables.VAR_CREATED_AT, ticket.getCreatedAt());
        variables.put(Variables.VAR_STATUS, TicketStatus.newTicket);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ticket", String.valueOf(ticket.getId()), variables);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_TICKET_ANSWERED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_TICKET_ANSWERED))
    @Transactional
    void sendMessageTicketIsAnswered(String ticketId) {
        processEngine.getRuntimeService().createMessageCorrelation(Messages.MSG_TICKET_ANSWERED)
                .processInstanceVariableEquals(Variables.VAR_TICKET_ID, Long.valueOf(ticketId))
                .correlateWithResult();
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_RESOLVER_ADDED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_RESOLVER_ADDED))
    @Transactional
    void sendMessageResolverIsAssignedToTicket(String ticketId) {
        Map<String, Object> variables = new HashMap<>();
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));
        String assignedUser = ticket.getAssignedUser();

        Task task = processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceBusinessKey(ticketId)
                .singleResult();
        variables.put(Variables.VAR_ASSIGNED_USER, assignedUser);
        variables.put(Variables.VAR_STATUS, TicketStatus.resolverAssigned);
        processEngine.getTaskService().complete(task.getId(), variables);
    }
}
