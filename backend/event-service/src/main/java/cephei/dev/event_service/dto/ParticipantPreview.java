package cephei.dev.event_service.dto;

public record ParticipantPreview (
        Integer userId,
        String firstname,
        String lastname,
        String username
) {
}
