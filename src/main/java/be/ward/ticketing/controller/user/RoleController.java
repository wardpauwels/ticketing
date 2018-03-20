package be.ward.ticketing.controller.user;

import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.service.TicketingService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private TicketingService ticketingService;

    @GetMapping("/roles")
    public List<Role> findAllRoles() {
        return Lists.newArrayList(ticketingService.findAllRoles());
    }
}