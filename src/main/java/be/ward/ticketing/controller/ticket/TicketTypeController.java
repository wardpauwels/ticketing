package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.TicketType;
import be.ward.ticketing.service.ticket.TicketTypeService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ticketypes")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    @Autowired
    public TicketTypeController(TicketTypeService ticketTypeService) {
        this.ticketTypeService = ticketTypeService;
    }

    @GetMapping
    public List<TicketType> findAllTicketTypes() {
        return Lists.newArrayList(ticketTypeService.findAllTicketTypes());
    }
}