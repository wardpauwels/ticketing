package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Domain;
import be.ward.ticketing.service.ticket.DomainService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/domains")
public class DomainController {

    private final DomainService domainService;

    @Autowired
    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    @GetMapping
    public List<Domain> findAllDomains() {
        return Lists.newArrayList(domainService.findAllDomains());
    }

}
