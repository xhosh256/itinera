package cephei.dev.profile_service.exception;

public class RequestAlreadyProceed extends RuntimeException {
    public RequestAlreadyProceed(String message) {
        super(message);
    }
}
