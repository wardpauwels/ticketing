package be.ward.ticketing.adapter;

import be.ward.ticketing.service.ticket.DomainService;
import be.ward.ticketing.service.ticket.PriorityService;
import be.ward.ticketing.service.ticket.TopicService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MakeCamundaTicketAdapter implements JavaDelegate {

    private final DomainService domainService;
    private final PriorityService priorityService;
    private final TopicService topicService;

    @Autowired
    public MakeCamundaTicketAdapter(DomainService domainService, PriorityService priorityService, TopicService topicService) {
        this.domainService = domainService;
        this.priorityService = priorityService;
        this.topicService = topicService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        delegateExecution.setVariable(Constants.VAR_DOMAIN_ID, domainService.findDomainWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_PRIORITY_ID, priorityService.findPriorityWithId(1L).getId());
        delegateExecution.setVariable(Constants.VAR_TOPIC_ID, topicService.findTopicWithId(1L).getId());
    }
}