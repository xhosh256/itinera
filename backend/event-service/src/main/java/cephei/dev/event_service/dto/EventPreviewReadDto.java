package cephei.dev.event_service.dto;

import cephei.dev.event_service.entity.Visibility;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EventPreviewReadDto(
        Long id,
        String hostUsername,
        String name,
        Integer capacity,
        Integer joined,
        Visibility visibility,

        String address,
        LocalDateTime startTime
) {
}
