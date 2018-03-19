package be.ward.ticketing.util;

public class TicketingException extends RuntimeException {

    public TicketingException(String message) {
        super(message);
    }

    public TicketingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TicketingException(Throwable cause) {
        super(cause);
    }
}
