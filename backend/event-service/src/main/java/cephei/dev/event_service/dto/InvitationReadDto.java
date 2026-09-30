package cephei.dev.event_service.dto;

import cephei.dev.event_service.entity.InvitationStatus;
import cephei.dev.event_service.entity.Visibility;

public record InvitationReadDto (
        Long id,
        String eventName,
        Visibility visibility,
        InvitationStatus status
) {
}
