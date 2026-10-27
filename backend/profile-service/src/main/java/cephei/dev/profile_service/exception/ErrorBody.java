package cephei.dev.profile_service.exception;
import org.springframework.http.HttpStatus;

public record ErrorBody
        (
                HttpStatus status,
                String message
        ){
}
