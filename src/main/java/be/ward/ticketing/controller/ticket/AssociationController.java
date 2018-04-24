package be.ward.ticketing.controller.ticket;

import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.service.ticket.AssociationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/associations")
public class AssociationController {

    private final AssociationService associationService;

    @Autowired
    public AssociationController(AssociationService associationService) {
        this.associationService = associationService;
    }

    @GetMapping
    public List<Association> getAllAssociations() {
        return associationService.getAllAssociations();
    }

    @GetMapping("/{associationId}")
    public Association findAssociationWithId(@PathVariable String associationId) {
        return associationService.findAssociation(Long.valueOf(associationId));
    }

    @GetMapping("/groups")
    public List<Association> getAllGroupedAssociations() {
        return associationService.getAllGroupedAssociations();
    }
}
