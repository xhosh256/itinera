package cephei.dev.profile_service.mapper;

import cephei.dev.profile_service.dto.FriendReadDto;
import cephei.dev.profile_service.dto.ProfileCreateDto;
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

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "friendIds", ignore = true)
    ProfileReadDto toReadDto(Profile profile);

    Profile toEntity(ProfileCreateDto profileCreateDto);
}
