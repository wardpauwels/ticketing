package be.ward.ticketing.util.mail;

import be.ward.ticketing.exception.MailException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SendMailTLSTest {

    @Test(expected = MailException.class)
    public void sendMailWithWrongEmailFormatTest() {
        SendMailTLS sendMailTLS = new SendMailTLS();
        sendMailTLS.sendMail("koptelefoon", "subject", "My mail");
    }

    @Test
    public void sendMailSuccesfull() {
        SendMailTLS sendMailTLS = new SendMailTLS();
        sendMailTLS.sendMail("hackerman@gmail.com", "Hackerman", "Hey Hackerman :)");
    }
}