package be.ward.ticketing.adapter;

import be.ward.ticketing.service.ticket.TicketService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CalculateDueDateAdapterTest {

    @Autowired
    private TicketService ticketService;

    private CalculateDueDateAdapter adapter;

    @Before
    public void setUp() {
        adapter = new CalculateDueDateAdapter(ticketService);
    }

    @Test
    public void calculateDueDateTest() {
        int days = 5;
        Date datePlusDays = adapter.getDatePlusDays(new Date(), days);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(sdf.format(new Date())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, days);  // number of days to add
        Date newDate = c.getTime();

        Assert.assertEquals(newDate, datePlusDays);
    }
}