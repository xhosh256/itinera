package cephei.dev.event_service.dto;

import cephei.dev.event_service.entity.InvitationStatus;
import cephei.dev.event_service.entity.Visibility;

public record OutcomingInvitationReadDto (
        Long id,
        String eventName,
        String username,
        Visibility visibility,
        InvitationStatus status
) {
}
