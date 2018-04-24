package be.ward.ticketing.adapter;

import be.ward.ticketing.util.ticket.Constants;
import be.ward.ticketing.util.ticket.Messages;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAssignedAdapter extends AssignedAdapter implements JavaDelegate {

    @Autowired
    public UserAssignedAdapter(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        super.execute(delegateExecution);
        rabbitTemplate.convertAndSend(Constants.VAR_DEFAULT_EXCHANGE, Messages.MSG_RESOLVER_ADDED, ticketId);
    }
}