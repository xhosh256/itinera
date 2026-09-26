package cephei.dev.authentication_service.dto;

import cephei.dev.authentication_service.entity.Role;

public record RegisterRequest (
        String username,
        String password,
        String email
) {
}
