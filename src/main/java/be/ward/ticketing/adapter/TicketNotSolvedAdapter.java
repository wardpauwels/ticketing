package be.ward.ticketing.adapter;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.util.Messages;
import be.ward.ticketing.util.TicketStatus;
import be.ward.ticketing.util.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class TicketNotSolvedAdapter implements JavaDelegate {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariable(Variables.VAR_STATUS, TicketStatus.ticketNotSolved);
        Long ticketId = (Long) delegateExecution.getVariable(Variables.VAR_TICKET_ID);

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_CLOSE_TICKET, ticketId);
    }

}
