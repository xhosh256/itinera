package cephei.dev.event_service.dto;

import java.time.LocalDateTime;

public record EventComponentReadDto
        (
                Long id,
                LocalDateTime startTime,
                LocalDateTime endTime,
                String description,
                String address
        ){
}
