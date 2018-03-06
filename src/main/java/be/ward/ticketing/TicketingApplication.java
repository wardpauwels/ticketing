package be.ward.ticketing;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableProcessApplication
@SpringBootApplication
public class TicketingApplication {

    public static void main(String... args) {
        SpringApplication.run(TicketingApplication.class, args);
    }

}