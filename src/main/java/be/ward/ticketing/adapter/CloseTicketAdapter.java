package be.ward.ticketing.adapter;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.util.ticket.Messages;
import be.ward.ticketing.util.ticket.TicketStatus;
import be.ward.ticketing.util.ticket.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CloseTicketAdapter implements JavaDelegate {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CloseTicketAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariable(Variables.VAR_STATUS, TicketStatus.ticketClosed);
        Long ticketId = (Long) delegateExecution.getVariable(Variables.VAR_TICKET_ID);

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_CLOSE_TICKET, ticketId);
    }
}
