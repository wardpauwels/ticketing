package be.ward.ticketing.conf;

import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.service.TicketingServiceImpl;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringBeansConfiguration {

    public final static String exchangeName = "ticketing";

    public TicketingService ticketingService() {
        return new TicketingServiceImpl();
    }
}
