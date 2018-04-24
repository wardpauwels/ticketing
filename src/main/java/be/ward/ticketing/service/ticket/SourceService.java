package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.SourceRepository;
import be.ward.ticketing.entities.ticketing.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {

    private final SourceRepository sourceRepository;

    @Autowired
    public SourceService(SourceRepository sourceRepository) {
        this.sourceRepository = sourceRepository;
    }

    public Iterable<Source> findAllSources() {
        return sourceRepository.findAll();
    }

    Source findSourceWithId(Long id) {
        return sourceRepository.findOne(id);
    }
}