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

    public final static String URL = "jdbc:mysql://localhost:3306/db_ticketingsystem?useSSL=false";
    public final static String USERNAME = "ticketmaster";
    public final static String PASSWORD = "mJ2CEk9EA7rDrNp0";

    @Bean
    public TicketingService ticketingService() {
        return new TicketingServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DataSource dataSource() {
//        BasicDataSource dataSource = new BasicDataSource();
//        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl(URL);
//        dataSource.setUsername(USERNAME);
//        dataSource.setPassword(PASSWORD);
//        return dataSource;
//    }
}
