package cephei.dev.authentication_service.dto;

import cephei.dev.authentication_service.entity.Role;

public record AuthMe (
        String username,
        Role role
) {
}
