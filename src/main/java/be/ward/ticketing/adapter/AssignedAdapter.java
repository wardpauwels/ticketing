package be.ward.ticketing.adapter;

import be.ward.ticketing.util.ticket.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class AssignedAdapter implements JavaDelegate {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssignedAdapter.class);

    final RabbitTemplate rabbitTemplate;

    Long ticketId;

    public AssignedAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        ticketId = (Long) delegateExecution.getVariable(Variables.VAR_TICKET_ID);
        LOGGER.debug("assignedAdapter");
    }
}
