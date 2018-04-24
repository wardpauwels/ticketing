package be.ward.ticketing.exception;

import be.ward.ticketing.util.mail.SendMailTLS;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailExceptionTest {

    @Test(expected = MailException.class)
    public void mailExceptionTest() {
        SendMailTLS sendMailTLS = new SendMailTLS();
        sendMailTLS.sendMail("ward", "My subject", "My text to you");
    }

}