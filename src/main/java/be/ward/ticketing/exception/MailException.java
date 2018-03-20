package be.ward.ticketing.exception;

public class MailException extends RuntimeException {
    public MailException() {
        super("Mail couldn't be sent");
    }
}
