package be.ward.ticketing.amqp;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Constants;
import be.ward.ticketing.util.Messages;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
public class CamundaReceiver {

    @Autowired
    private TicketingService ticketingService;

    @Autowired
    private ProcessEngine processEngine;

    @RabbitListener(bindings = @QueueBinding( //
            value = @Queue(value = Messages.MSG_NEW_TICKET, durable = "true"), //
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"), //
            key = "*"))
    @Transactional
    void newMessageMade(String ticketId) {
        Ticket ticket = ticketingService.findTicket(Long.valueOf(ticketId));

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put(Constants.VAR_TICKET_ID, ticket.getId());
        variables.put(Constants.VAR_CREATOR, ticket.getCreator());
        variables.put(Constants.VAR_CREATED_AT, ticket.getCreatedAt());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("ticket", variables);
    }

    @RabbitListener(bindings = @QueueBinding( //
            value = @Queue(value = Messages.MSG_TICKET_ANSWERED, durable = "true"), //
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"), //
            key = "*"))
    @Transactional
    void sendAnswerOnTicket(String ticketId) {

        TaskService taskService = processEngine.getTaskService();

    }
}
