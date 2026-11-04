package cephei.dev.profile_service.exception;

public class FriendshipNotFound extends RuntimeException {
    public FriendshipNotFound(String friendshipNotFound) {
        super(friendshipNotFound);
    }
}
