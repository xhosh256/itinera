package cephei.dev.profile_service.dto;

import lombok.Builder;

@Builder
public record FriendReadDto(
    Integer id,
    String firstname,
    String lastname

) {
}
