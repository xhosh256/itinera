package cephei.dev.event_service.dto;

public record EventCreateRequest(
        String name,
        Integer capacity
) {
}
