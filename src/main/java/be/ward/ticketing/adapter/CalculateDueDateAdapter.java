package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.service.ticket.TicketService;
import be.ward.ticketing.util.ticket.Constants;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class CalculateDueDateAdapter implements JavaDelegate {

    private final TicketService ticketService;

    @Autowired
    public CalculateDueDateAdapter(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));
        int days = (int) delegateExecution.getVariable(Constants.VAR_DUE_AT_DAYS);
        ticket.setDueAt(getDatePlusDays(ticket.getCreatedAt(), days));
        ticketService.saveTicket(ticket);
    }

    Date getDatePlusDays(Date date, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //negative days will decrement the days
        return cal.getTime();
    }
}