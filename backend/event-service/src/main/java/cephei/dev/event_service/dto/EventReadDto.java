package cephei.dev.event_service.dto;


import java.util.Set;

public record EventReadDto (
        Long id,
        String name,
        Integer capacity,
        Set<Integer> participantIds
) {
}
