package cephei.dev.profile_service.dto;

import lombok.Builder;

@Builder
public record FriendReadDto(
    Integer id,
    Integer userId,
    String username,
    String firstname,
    String lastname

) {
}
