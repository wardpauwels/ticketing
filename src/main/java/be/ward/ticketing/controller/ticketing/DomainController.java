package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.entities.ticketing.Domain;
import be.ward.ticketing.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DomainController {

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/domains")
    public List<Domain> findAllDomains() {
        List<Domain> domains = new ArrayList<>();

        for (Domain domain : ticketingService.findAllDomains()) {
            domains.add(domain);
        }

        return domains;
    }

}
