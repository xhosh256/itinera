package cephei.dev.event_service.dto;

import cephei.dev.event_service.entity.Visibility;

public record EventPreviewReadDto(
        Long id,
        String name,
        Integer capacity,
        Integer joined,
        Visibility visibility
) {
}
