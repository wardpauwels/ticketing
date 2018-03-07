package be.ward.ticketing.adapter;

import be.ward.ticketing.entities.Ticket;
import be.ward.ticketing.mail.SendMailTLS;
import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.util.Constants;
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
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Ticket ticket = ticketingService.findTicket((Long) delegateExecution.getVariable(Constants.VAR_TICKET_ID));
        SendMailTLS mail = new SendMailTLS();
        //TODO: Get mail address from the user
        mail.sendMail(
                "wardpauwels@hotmail.be",
                "Your ticket has been answered",
                "Your ticket has been answered. Your question was: " + ticket.getDescription()
        );
    }
}
