package cephei.dev.profile_service.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ProfileReadDto
        (
                String firstname,
                String lastname,
                LocalDate birthDate,
                String bio
        ){
}
