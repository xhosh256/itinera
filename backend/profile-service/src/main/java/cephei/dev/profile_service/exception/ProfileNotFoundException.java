package cephei.dev.profile_service.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String profileNotFound) {
        super(profileNotFound);
    }
}
