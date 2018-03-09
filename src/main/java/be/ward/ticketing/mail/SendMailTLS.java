package be.ward.ticketing.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailTLS {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMailTLS.class);
    private static final String HOST = "smtp.gmail.com";
    private static final String USER = "camundamailtest@gmail.com";
    private static final String PWD = "camundamail";

    public SendMailTLS() {
    }

    public void sendMail(String to, String subject, String mailText) {
        String username = "camundamailtest@gmail.com";
        String password = "camundamail";


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(mailText);

            Transport.send(message);

            System.out.println("Mail sent!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}