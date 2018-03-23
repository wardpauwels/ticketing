package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTicketsDbAdapter implements JavaDelegate {

    private final TicketingService ticketingService;

    @Autowired
    public UpdateTicketsDbAdapter(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));

        ticket.setAssignedGroup((String) delegateExecution.getVariable(Constants.VAR_ASSIGNED_GROUP));
        ticket.setAssignedUser((String) delegateExecution.getVariable(Constants.VAR_ASSIGNED_USER));
        ticket.setPriority(
                ticketingService.findPriorityByName(
                        (String) delegateExecution.getVariable(Constants.VAR_PRIORITY)
                )
        );
        ticketingService.saveTicket(ticket);
    }
}