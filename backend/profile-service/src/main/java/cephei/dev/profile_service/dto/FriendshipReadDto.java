package cephei.dev.profile_service.dto;

import cephei.dev.profile_service.entity.FriendshipStatus;
import lombok.Builder;

@Builder
public record FriendshipReadDto (
        Long id,
        ProfileReadDto user,
        ProfileReadDto friend,
        FriendshipStatus status
) {
}
