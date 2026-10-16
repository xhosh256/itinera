package cephei.dev.event_service.dto;

import java.time.LocalDateTime;

public record EventComponentEditDto(
        LocalDateTime startTime,
        LocalDateTime endTime,
        String description,
        String address
) {
}
