package be.ward.ticketing.controller.ticketing;

import be.ward.ticketing.entities.ticketing.Priority;
import be.ward.ticketing.service.TicketingService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PriorityController {

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/priorities")
    public List<Priority> findAllPriorities() {
        return Lists.newArrayList(ticketingService.findAllPriorities());
    }

}
