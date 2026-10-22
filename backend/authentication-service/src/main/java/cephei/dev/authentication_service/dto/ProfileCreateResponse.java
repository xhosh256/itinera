package cephei.dev.authentication_service.dto;

public record ProfileCreateResponse (
        Integer userId,
        String firstname,
        String lastname
) {
}
