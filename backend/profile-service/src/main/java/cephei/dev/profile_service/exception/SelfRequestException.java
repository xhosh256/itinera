package cephei.dev.profile_service.exception;

public class SelfRequestException extends RuntimeException {
    public SelfRequestException(String message) {
        super(message);
    }
}
