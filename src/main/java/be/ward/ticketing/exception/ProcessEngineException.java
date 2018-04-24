package be.ward.ticketing.exception;

public class ProcessEngineException extends RuntimeException {
    public ProcessEngineException() {
        super("Couldn't start process engine");
    }
}