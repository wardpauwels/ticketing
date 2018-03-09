import be.ward.ticketing.adapter.CalculateDueDateAdapter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class Ticketing {

    @Test
    public void calculateDueDateTest() {
        CalculateDueDateAdapter adapter = new CalculateDueDateAdapter();
        Date dueDate = new Date();
        Date newDate = adapter.getDatePlusDays(dueDate, 0);
        Assert.assertEquals(dueDate, newDate);
    }

}
