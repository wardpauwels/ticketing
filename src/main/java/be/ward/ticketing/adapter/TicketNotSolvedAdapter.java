package be.ward.ticketing.adapter;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Messages;
import be.ward.ticketing.util.ticket.TicketStatus;
import be.ward.ticketing.util.ticket.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketNotSolvedAdapter implements JavaDelegate {

    private final TicketingService ticketingService;

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TicketNotSolvedAdapter(TicketingService ticketingService, RabbitTemplate rabbitTemplate) {
        this.ticketingService = ticketingService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariable(Variables.VAR_STATUS, TicketStatus.ticketNotSolved);
        String comment = (String) delegateExecution.getVariable(Variables.VAR_COMMENT);

        String user = (String) delegateExecution.getVariable(Variables.VAR_CREATOR);

        Ticket ticket = ticketingService.createTicket(user, comment);
        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_NEW_TICKET, ticket.getId());
    }
}