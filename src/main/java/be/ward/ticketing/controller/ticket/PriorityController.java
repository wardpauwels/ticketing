package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Priority;
import be.ward.ticketing.service.ticket.PriorityService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/priorities")
public class PriorityController {

    private final PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping
    public List<Priority> findAllPriorities() {
        return Lists.newArrayList(priorityService.findAllPriorities());
    }

}
