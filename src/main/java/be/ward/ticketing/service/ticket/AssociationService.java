package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.AssociationRepository;
import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AssociationService {

    private final AssociationRepository associationRepository;

    @Autowired
    public AssociationService(AssociationRepository associationRepository) {
        this.associationRepository = associationRepository;
    }

    public Association findAssociation(Long associationId) {
        return associationRepository.findOne(associationId);
    }

    public Association createAssociation(String associationType, Ticket ticket) {
        Association association = new Association(associationType, ticket);
        return associationRepository.save(association);
    }

    Association linkNewAssociationToAssociation(Association newAssociation, Association oldAssociation) {
        Association association = findAssociationById(oldAssociation.getId());
        association.setAssociation(newAssociation);
        return associationRepository.save(association);
    }

    public List<Association> getAllAssociations() {
        return Lists.newArrayList(associationRepository.findAll());
    }

    public Association getTopAssociationFromTicket(Ticket ticket) {
        Association association = findAssociationByTicket(ticket);
        Association aboveAssociation = findAssociationByAssociation(association);
        while (aboveAssociation != null) {
            association = aboveAssociation;
            aboveAssociation = findAssociationByAssociation(association);
            if (aboveAssociation == null) return association;
        }
        return association;
    }

    // TODO: Use optional and remove while loop
    public Association getLastAssociationFromTicket(Ticket ticket) {
        Association association = findAssociationByTicket(ticket);
        while (association.getAssociation() != null) {
            association = association.getAssociation();
        }
        return association;
    }

    public Association findAssociationById(Long associationId) {
        return associationRepository.findOne(associationId);
    }

    private Association findAssociationByTicket(Ticket ticket) {
        return associationRepository.findByTicket(ticket);
    }

    private Association findAssociationByAssociation(Association association) {
        return associationRepository.findByAssociation(association);
    }

    public List<Association> getAllGroupedAssociations() {
        List<Association> allAssociations = getAllAssociations();
        List<Association> topAssociations = new ArrayList<>();
        for (Association association : allAssociations) {
            Association topAssociation = getTopAssociationFromTicket(association.getTicket());
            if (!topAssociations.contains(topAssociation)) {
                topAssociations.add(topAssociation);
            }
        }
        return topAssociations;
    }
}