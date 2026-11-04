package cephei.dev.event_service.exception;

public class InvitationRequestAlreadySent extends RuntimeException {
    public InvitationRequestAlreadySent(String message) {
        super(message);
    }
}
