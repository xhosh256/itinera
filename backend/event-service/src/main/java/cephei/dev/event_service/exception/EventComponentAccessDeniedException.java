package cephei.dev.event_service.exception;

public class EventComponentAccessDeniedException extends RuntimeException {
    public EventComponentAccessDeniedException(String message) {
        super(message);
    }
}
