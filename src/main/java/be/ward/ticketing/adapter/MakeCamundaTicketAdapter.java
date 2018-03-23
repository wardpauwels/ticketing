package be.ward.ticketing.adapter;

import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MakeCamundaTicketAdapter implements JavaDelegate {

    private final TicketingService ticketingService;

    @Autowired
    public MakeCamundaTicketAdapter(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariable(Constants.VAR_DOMAIN_ID, ticketingService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_PRIORITY_ID, ticketingService.findPriorityWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_TOPIC_ID, ticketingService.findTopicWithId(1L).getId());
    }
}