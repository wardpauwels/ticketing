package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import be.ward.ticketing.util.ticket.TicketStatus;
import org.camunda.bpm.engine.ProcessEngine;
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
public class ResolverAddedReceiver {

    private final TicketingService ticketingService;
    private final ProcessEngine processEngine;

    @Autowired
    public ResolverAddedReceiver(TicketingService ticketingService, ProcessEngine processEngine) {
        this.ticketingService = ticketingService;
        this.processEngine = processEngine;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_RESOLVER_ADDED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_RESOLVER_ADDED))
    @Transactional
    void sendMessageResolverIsAssignedToTicket(String ticketId) {
        String assignedUser = ticketingService.findTicket(Long.valueOf(ticketId)).getAssignedUser();

        Task task = processEngine.getTaskService().createTaskQuery()
                .processInstanceBusinessKey(ticketId).singleResult();

        Map<String, Object> variables = new HashMap<>();
        variables.put(Constants.VAR_ASSIGNED_USER, assignedUser);
        variables.put(Constants.VAR_STATUS, TicketStatus.resolverAssigned);

        processEngine
                .getTaskService()
                .complete(task.getId(), variables);
    }
}