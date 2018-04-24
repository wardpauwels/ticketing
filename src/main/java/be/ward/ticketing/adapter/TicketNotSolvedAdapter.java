package be.ward.ticketing.adapter;

import be.ward.ticketing.service.ticket.TicketService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TicketNotSolvedAdapter implements JavaDelegate {

    private final TicketService ticketService;

    @Autowired
    public TicketNotSolvedAdapter(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Long ticketId = (Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID);
        String user = (String) delegateExecution.getVariable(Constants.VAR_CREATOR);
        String answer = (String) delegateExecution.getVariable(Constants.VAR_COMMENT);

        ticketService.answerOnTicketWithId(ticketId, user, answer);
    }
}