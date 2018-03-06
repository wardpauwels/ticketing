package be.ward.ticketing.adapter;

import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class MakeCamundaTicketAdapter implements JavaDelegate {

    @Autowired
    @Qualifier("ticketingServiceImpl")
    private TicketingService ticketingService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        delegateExecution.setVariable(Constants.VAR_DOMAIN_ID, ticketingService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_SOURCE_ID, ticketingService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_TICKET_TYPE_ID, ticketingService.findTicketTypeWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_TOPIC_ID, ticketingService.findTopicWithId(1L).getId());

    }
}
