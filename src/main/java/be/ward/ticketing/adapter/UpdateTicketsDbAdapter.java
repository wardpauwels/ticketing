package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UpdateTicketsDbAdapter implements JavaDelegate {

    private final TicketingService ticketingService;

    @Autowired
    public UpdateTicketsDbAdapter(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Variables.VAR_TICKET_ID));

        ticket.setAssignedGroup((String) delegateExecution.getVariable(Variables.VAR_ASSIGNED_GROUP));
        ticket.setAssignedUser((String) delegateExecution.getVariable(Variables.VAR_ASSIGNED_USER));
        ticket.setDueAt((Date) delegateExecution.getVariable(Variables.VAR_DUE_AT));
        ticket.setPriority(
                ticketingService.findPriorityByName(
                        (String) delegateExecution.getVariable(Variables.VAR_PRIORITY)
                )
        );
        ticket.setStatus((String) delegateExecution.getVariable(Variables.VAR_STATUS));
        ticketingService.saveTicket(ticket);
    }
}