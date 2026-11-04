package cephei.dev.profile_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
public record ProfileReadDto
        (
                Integer id,
                String firstname,
                String lastname,
                LocalDate birthDate,
                String bio,
                Set<Integer> friendIds
        ){
}
