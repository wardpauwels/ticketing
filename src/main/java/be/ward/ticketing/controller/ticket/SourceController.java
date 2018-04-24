package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Source;
import be.ward.ticketing.service.ticket.SourceService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sources")
public class SourceController {

    private final SourceService sourceService;

    @Autowired
    public SourceController(SourceService sourceService) {
        this.sourceService = sourceService;
    }

    @GetMapping
    public List<Source> findAllSources() {
        return Lists.newArrayList(sourceService.findAllSources());
    }
}