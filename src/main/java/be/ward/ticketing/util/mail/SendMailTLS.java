package be.ward.ticketing.util.mail;

import be.ward.ticketing.exception.MailException;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendMailTLS {

    private String username = "camundamailtest@gmail.com";

    private String password = "camundamail";

    public SendMailTLS() {
    }

    public void sendMail(String to, String subject, String mailText) {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(mailText);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new MailException();
        }
    }
}