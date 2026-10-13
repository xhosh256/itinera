package cephei.dev.event_service.dto;


import cephei.dev.event_service.entity.Visibility;

import java.util.List;
import java.util.Set;

public record EventReadDto (
        Long id,
        String name,
        Integer capacity,
        Visibility visibility,
        List<EventComponentReadDto> components,
        Set<Integer> participantIds
) {
}
