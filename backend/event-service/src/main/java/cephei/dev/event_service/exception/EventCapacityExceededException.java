package cephei.dev.event_service.exception;

public class EventCapacityExceededException extends RuntimeException {
    public EventCapacityExceededException(String eventCapacityExceeded) {
        super(eventCapacityExceeded);
    }
}
