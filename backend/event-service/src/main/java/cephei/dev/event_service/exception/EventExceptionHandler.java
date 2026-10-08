package cephei.dev.event_service.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(EventAccessDeniedException.class)
    public ResponseEntity<String> eventAccessDenied(EventAccessDeniedException e) {
        return ResponseEntity.status(403).body("Access Denied!");
    }
}
