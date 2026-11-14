package cephei.dev.profile_service.dto;

public record ProfilePreviewReadDto(
        Integer userId,
        String username,
        String firstname,
        String lastname
) {
}
