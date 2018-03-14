package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.mail.SendMailTLS;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.TicketStatus;
import be.ward.ticketing.util.Variables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class EmailCustomerAdapter implements JavaDelegate {

    @Autowired
    private TicketingService ticketingService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Variables.VAR_TICKET_ID));
        SendMailTLS mail = new SendMailTLS();
        //TODO: Get mail address from the user
        mail.sendMail(
                "wardpauwels@hotmail.be",
                "Your ticket has been answered",
                "Your ticket has been answered.\n Your question was: \n" + ticket.getDescription()
        );
        delegateExecution.setVariable(Variables.VAR_STATUS, TicketStatus.mailSent);
    }
}
