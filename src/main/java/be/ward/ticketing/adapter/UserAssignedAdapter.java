package be.ward.ticketing.adapter;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.util.ticket.Messages;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAssignedAdapter extends AssignedAdapter implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAssignedAdapter.class);

    @Autowired
    public UserAssignedAdapter(RabbitTemplate rabbitTemplate) {
        super(rabbitTemplate);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        super.execute(delegateExecution);
        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_RESOLVER_ADDED, ticketId);
        LOGGER.debug("UserAssignedAdapter");
    }
}