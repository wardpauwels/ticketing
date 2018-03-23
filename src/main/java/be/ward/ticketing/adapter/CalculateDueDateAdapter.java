package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class CalculateDueDateAdapter implements JavaDelegate {

    private final TicketingService ticketingService;

    @Autowired
    public CalculateDueDateAdapter(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));
        int days = (int) delegateExecution.getVariable(Constants.VAR_DUE_AT_DAYS);
        ticket.setDueAt(getDatePlusDays(ticket.getCreatedAt(), days));
        ticketingService.saveTicket(ticket);
    }

    public Date getDatePlusDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //negative days will decrement the days
        return cal.getTime();
    }
}