package cephei.dev.authentication_service.dto;

import cephei.dev.authentication_service.entity.Role;

public record AuthMe (
        Integer id,
        String username,
        Role role
) {
}
