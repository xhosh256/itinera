package cephei.dev.event_service.exception;

public class UserNotInEvent extends RuntimeException {
    public UserNotInEvent(String s) {
        super(s);
    }
}
