package cephei.dev.authentication_service.dto;

public record RegisterRequest (
        String username,
        String password,
        String firstname,
        String lastname,
        String email
) {
}
