package cephei.dev.event_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvitationExceptionHandler {

    @ExceptionHandler(InvitationAccessDeniedException.class)
    public ResponseEntity<String> invitationAccessDenied(InvitationAccessDeniedException e) {
        return ResponseEntity.status(403).body("Access Denied!");
    }
}
