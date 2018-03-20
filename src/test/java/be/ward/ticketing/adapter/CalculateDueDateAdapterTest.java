package be.ward.ticketing.adapter;

import be.ward.ticketing.service.TicketingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CalculateDueDateAdapterTest {

    @Autowired
    private TicketingService ticketingService;

    @Test
    public void calculateDueDateTest() {
        CalculateDueDateAdapter adapter = new CalculateDueDateAdapter(ticketingService);
        Date dueDate = new Date();
        Date newDate = adapter.getDatePlusDays(dueDate, 0);
        Assert.assertEquals(dueDate, newDate);
    }
}