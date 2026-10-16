package cephei.dev.event_service.exception;

public class EventComponentNotFoundException extends RuntimeException {
    public EventComponentNotFoundException(String message) {
        super(message);
    }
}
