package be.ward.ticketing.adapter;

import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MakeCamundaTicketAdapter implements JavaDelegate {

    @Autowired
    private TicketingService ticketingService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.setVariable(Variables.VAR_STATUS, "New Ticket");
        delegateExecution.setVariable(Variables.VAR_DOMAIN_ID, ticketingService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Variables.VAR_SOURCE_ID, ticketingService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Variables.VAR_TICKET_TYPE_ID, ticketingService.findTicketTypeWithId(1L).getId());
        delegateExecution.setVariable(Variables.VAR_TOPIC_ID, ticketingService.findTopicWithId(1L).getId());

    }
}
