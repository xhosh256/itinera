package cephei.dev.profile_service.mapper;

import cephei.dev.profile_service.dto.FriendReadDto;
import cephei.dev.profile_service.dto.ProfileCreateDto;
import cephei.dev.profile_service.dto.ProfilePreviewReadDto;
import cephei.dev.profile_service.dto.ProfileReadDto;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.repository.FriendshipRepository;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.*;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "friendIds", ignore = true)
    default ProfileReadDto toReadDto(Profile profile, String username, Set<Integer> friendIds) {
        return new ProfileReadDto(
                profile.getId(),
                username,
                profile.getFirstname(),
                profile.getLastname(),
                profile.getBirthDate(),
                profile.getBio(),
                friendIds
        );
    }

    Profile toEntity(ProfileCreateDto profileCreateDto);

    default ProfilePreviewReadDto toPreview(Profile profile, String username) {
        return new ProfilePreviewReadDto(
                profile.getUserId(),
                username,
                profile.getFirstname(),
                profile.getLastname()
        );
    }

    default FriendReadDto toFriendReadDto(Profile friend, String username) {
        return new FriendReadDto(
                friend.getId(),
                friend.getUserId(),
                username,
                friend.getFirstname(),
                friend.getLastname()
        );
    };
}
