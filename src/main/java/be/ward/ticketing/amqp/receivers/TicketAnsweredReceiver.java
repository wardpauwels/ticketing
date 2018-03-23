package be.ward.ticketing.amqp.receivers;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Messages;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TicketAnsweredReceiver {

    private final TicketingService ticketingService;

    private final ProcessEngine processEngine;

    @Autowired
    public TicketAnsweredReceiver(ProcessEngine processEngine, TicketingService ticketingService) {
        this.processEngine = processEngine;
        this.ticketingService = ticketingService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = Messages.MSG_TICKET_ANSWERED, durable = "true"),
            exchange = @Exchange(value = SpringBeansConfiguration.exchangeName, type = "topic", durable = "true"),
            key = Messages.MSG_TICKET_ANSWERED))
    @Transactional
    void sendMessageTicketIsAnswered(String mainTicketId) {
        Ticket mainTicket = ticketingService.findTicket(Long.valueOf(mainTicketId));

        processEngine
                .getRuntimeService()
                .createMessageCorrelation(Messages.MSG_TICKET_ANSWERED)
                .processInstanceBusinessKey(String.valueOf(mainTicket.getId()))
                .correlateWithResult();
    }
}