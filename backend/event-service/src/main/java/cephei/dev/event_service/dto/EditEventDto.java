package cephei.dev.event_service.dto;

import cephei.dev.event_service.entity.Visibility;

public record EditEventDto (
        String name,
        Integer capacity,
        Visibility visibility
) {
}
