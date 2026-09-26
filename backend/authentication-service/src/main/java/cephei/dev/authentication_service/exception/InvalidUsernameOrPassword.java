package cephei.dev.authentication_service.exception;

public class InvalidUsernameOrPassword extends RuntimeException {
    public InvalidUsernameOrPassword(String invalidUsernameOrPassword) {
        super(invalidUsernameOrPassword);
    }
}
