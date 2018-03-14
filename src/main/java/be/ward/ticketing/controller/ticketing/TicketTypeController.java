package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.entities.ticketing.TicketType;
import be.ward.ticketing.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketTypeController {

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/tickettypes")
    public List<TicketType> findAllTicketTypes() {
        List<TicketType> ticketTypes = new ArrayList<>();

        for (TicketType ticketType : ticketingService.findAllTicketTypes()) {
            ticketTypes.add(ticketType);
        }
        return ticketTypes;
    }

}
