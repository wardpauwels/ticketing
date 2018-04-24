package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.TicketTypeRepository;
import be.ward.ticketing.entities.ticketing.TicketType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    @Autowired
    public TicketTypeService(TicketTypeRepository ticketTypeRepository) {
        this.ticketTypeRepository = ticketTypeRepository;
    }

    public Iterable<TicketType> findAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    TicketType findTicketTypeWithId(Long id) {
        return ticketTypeRepository.findOne(id);
    }
}