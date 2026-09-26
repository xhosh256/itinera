package cephei.dev.authentication_service.dto;

public record LoginRequest(
        String username,
        String password
) {
}
