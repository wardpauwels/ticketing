package be.ward.ticketing.exception;

public class NoTaskFoundException extends RuntimeException {
    public NoTaskFoundException() {
        super("No task found");
    }
}
