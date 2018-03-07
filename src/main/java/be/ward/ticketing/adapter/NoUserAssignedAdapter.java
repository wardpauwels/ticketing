package be.ward.ticketing.adapter;

import be.ward.ticketing.conf.SpringBeansConfiguration;
import be.ward.ticketing.util.Constants;
import be.ward.ticketing.util.Messages;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class NoUserAssignedAdapter implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(NoUserAssignedAdapter.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Long ticketId = (Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID);

        logger.debug("------------------------- Message -------------------------");

        rabbitTemplate.convertAndSend(SpringBeansConfiguration.exchangeName, Messages.MSG_NO_RESOLVER, ticketId);

    }
}
