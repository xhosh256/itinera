package cephei.dev.event_service.exception;

public class EventAccessDeniedException extends RuntimeException {
    public EventAccessDeniedException(String s) {
        super(s);
    }
}
