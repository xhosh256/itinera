package cephei.dev.profile_service.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileEditDto(
        String firstname,
        String lastname,
        LocalDate birthDate,
        String bio
) {
}
