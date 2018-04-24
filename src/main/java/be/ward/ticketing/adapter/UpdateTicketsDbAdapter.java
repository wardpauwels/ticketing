package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.PriorityService;
import be.ward.ticketing.service.ticket.TicketService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateTicketsDbAdapter implements JavaDelegate {

    private final TicketService ticketingService;
    private final PriorityService priorityService;

    @Autowired
    public UpdateTicketsDbAdapter(TicketService ticketingService, PriorityService priorityService) {
        this.ticketingService = ticketingService;
        this.priorityService = priorityService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));

        ticket.setAssignedGroup((String) delegateExecution.getVariable(Constants.VAR_ASSIGNED_GROUP));
        ticket.setAssignedUser((String) delegateExecution.getVariable(Constants.VAR_ASSIGNED_USER));
        ticket.setPriority(
                priorityService.findPriorityByName(
                        (String) delegateExecution.getVariable(Constants.VAR_PRIORITY)
                )
        );
        ticketingService.saveTicket(ticket);
    }
}