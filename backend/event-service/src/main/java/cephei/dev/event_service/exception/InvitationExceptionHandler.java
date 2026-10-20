package cephei.dev.event_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvitationExceptionHandler {

    @ExceptionHandler(InvitationAccessDeniedException.class)
    public ResponseEntity<ErrorBody> invitationAccessDenied(InvitationAccessDeniedException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );

        return ResponseEntity.status(403).body(errorBody);
    }

    @ExceptionHandler(InvitationNotFoundException.class)
    public ResponseEntity<ErrorBody> invitationNotFound(InvitationNotFoundException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }
}
