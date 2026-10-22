package cephei.dev.profile_service.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileCreateDto(
        Integer userId,
        String firstname,
        String lastname,
        LocalDate birthDate,
        String bio
) {
}
