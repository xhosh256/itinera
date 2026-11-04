package cephei.dev.profile_service.dto;

import lombok.Builder;

@Builder
public record FriendReadDto(
    Integer id,
    Integer userId,
    String firstname,
    String lastname

) {
}
