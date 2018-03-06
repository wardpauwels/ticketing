package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Constants;
import org.apache.tomcat.util.bcel.Const;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@ConfigurationProperties
public class CalculateDueDateAdapter implements JavaDelegate{

    @Autowired
    private TicketingService ticketingService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));
        int days = (int) delegateExecution.getVariable(Constants.VAR_DUE_AT_DAYS);
        ticket.setDueAt(getDatePlusDays(ticket.getCreatedAt(), days));

        delegateExecution.setVariable(Constants.VAR_DUE_AT, ticket.getDueAt());
    }

    public Date getDatePlusDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
}
