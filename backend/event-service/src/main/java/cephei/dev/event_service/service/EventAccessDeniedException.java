package cephei.dev.event_service.service;

public class EventAccessDeniedException extends RuntimeException {
    public EventAccessDeniedException(String s) {
        super(s);
    }
}
