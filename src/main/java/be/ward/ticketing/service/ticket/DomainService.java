package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.DomainRepository;
import be.ward.ticketing.entities.ticketing.Domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

    private final DomainRepository domainRepository;

    @Autowired
    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public Iterable<Domain> findAllDomains() {
        return domainRepository.findAll();
    }

    public Domain findDomainWithId(Long id) {
        return domainRepository.findOne(id);
    }
}