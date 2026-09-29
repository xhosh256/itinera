package cephei.dev.event_service.exception;

public class InvitationNotFoundException extends RuntimeException {
    public InvitationNotFoundException(String invitationNotFound) {
        super(invitationNotFound);
    }
}
