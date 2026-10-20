package cephei.dev.event_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EventExceptionHandler {

    @ExceptionHandler(EventAccessDeniedException.class)
    public ResponseEntity<ErrorBody> eventAccessDenied(EventAccessDeniedException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );

        return ResponseEntity.status(403).body(errorBody);
    }

    @ExceptionHandler(EventNotFound.class)
    public ResponseEntity<ErrorBody> eventNotFound(EventNotFound e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        return ResponseEntity.status(404).body(errorBody);
    }

    @ExceptionHandler(AlreadyTakingPartException.class)
    public ResponseEntity<ErrorBody> alreadyTakingPart(AlreadyTakingPartException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(EventCapacityExceededException.class)
    public ResponseEntity<ErrorBody> eventCapacityExceeded(EventCapacityExceededException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody);
    }

    @ExceptionHandler(EventComponentAccessDeniedException.class)
    public ResponseEntity<ErrorBody> eventComponentAccessDenied(EventComponentAccessDeniedException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.FORBIDDEN,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBody);
    }

    @ExceptionHandler(EventComponentNotFoundException.class)
    public ResponseEntity<ErrorBody> eventComponentNotFound(EventComponentNotFoundException e) {
        ErrorBody errorBody = new ErrorBody(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
    }

}
