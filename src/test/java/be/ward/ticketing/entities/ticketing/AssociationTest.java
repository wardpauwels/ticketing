package be.ward.ticketing.entities.ticketing;

import be.ward.ticketing.util.ticket.AssociationTypes;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class AssociationTest {

    @Test
    public void associationTest() {
        Association subAssociation = new Association();
        Ticket ticket = new Ticket();
        Association association = new Association(AssociationTypes.ticket, ticket);

        assertEquals(AssociationTypes.ticket, association.getAssociationType());

        association.setAssociationType(AssociationTypes.answer);
        association.setTicket(ticket);
        association.setAssociation(subAssociation);
        association.setLockVersion(1);

        assertNull(association.getId());
        assertEquals(AssociationTypes.answer, association.getAssociationType());
        assertEquals(ticket, association.getTicket());
        assertEquals(subAssociation, association.getAssociation());
        assertEquals(1, (int) association.getLockVersion());
    }
}