package cephei.dev.profile_service.exception;

public class FriendshipRequestAlreadyPendingException extends RuntimeException {
    public FriendshipRequestAlreadyPendingException(String message) {
        super(message);
    }
}
