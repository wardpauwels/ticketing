package be.ward.ticketing.service.ticket;

import be.ward.ticketing.data.ticketing.AssociationRepository;
import be.ward.ticketing.entities.ticketing.Association;
import be.ward.ticketing.entities.ticketing.Ticket;
import be.ward.ticketing.util.ticket.AssociationTypes;
import be.ward.ticketing.util.ticket.TicketStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class AssociationServiceTest {

    @Mock
    private AssociationRepository associationRepository;

    @InjectMocks
    private AssociationService associationService;

    private Ticket firstTicket;
    private Ticket secondTicket;
    private Association firstAssociation;
    private Association secondAssociation;

    @Before
    public void setUp() {
        Date date = new Date();

        firstTicket = new Ticket(5L);
        firstTicket.setStatus(TicketStatus.ticketClosed);
        firstTicket.setCreator("Bert");
        firstTicket.setAssignedUser("Bram");
        firstTicket.setDescription("Mijn lampen werken niet.");

        firstAssociation = new Association(1L, AssociationTypes.ticket, firstTicket);

        secondTicket = new Ticket(9L);
        secondTicket.setStatus(TicketStatus.newTicket);
        secondTicket.setCreator("Flor");
        secondTicket.setAssignedUser("Bram");
        secondTicket.setCreatedAt(date);
        secondTicket.setDescription("Steek de stekker in.");

        secondAssociation = new Association(2L, AssociationTypes.answer, secondTicket);
        firstAssociation.setAssociation(secondAssociation);
    }

    @Test
    public void findAssociationByIdTest() {

    }


}