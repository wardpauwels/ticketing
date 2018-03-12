package be.ward.ticketing.conf;

import be.ward.ticketing.service.TicketingService;
import be.ward.ticketing.service.TicketingServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class SpringBeansConfiguration {

    public final static String exchangeName = "ticketing";

    @Bean
    public TicketingService ticketingService() {
        return new TicketingServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
