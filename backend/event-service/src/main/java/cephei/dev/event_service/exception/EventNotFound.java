package cephei.dev.event_service.exception;

public class EventNotFound extends RuntimeException {
    public EventNotFound(String eventNotFound) {
        super(eventNotFound);
    }
}
