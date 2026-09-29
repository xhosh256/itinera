package cephei.dev.event_service.exception;

public class InvitationAccessDeniedException extends RuntimeException {
    public InvitationAccessDeniedException(String invitationAccessDenied) {
        super(invitationAccessDenied);
    }
}
