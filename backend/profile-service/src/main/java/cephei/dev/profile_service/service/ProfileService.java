package cephei.dev.profile_service.service;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.*;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.ProfileNotFoundException;
import cephei.dev.profile_service.mapper.ProfileMapper;
import cephei.dev.profile_service.repository.FriendshipRepository;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final FriendshipRepository friendshipRepository;
    private final UserClient userClient;

    public ProfileReadDto findByUserId(Integer userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        Set<Integer> friendIds = new HashSet<>();

        for (Friendship friendship : friendshipRepository.findAllByProfileIdAndStatus(
                FriendshipStatus.ACCEPTED, profile.getId())
        ) {
            Profile friendProfile = Objects.equals(friendship.getUser().getId(), profile.getId())
                    ? friendship.getFriend()
                    : friendship.getUser();

            friendIds.add(friendProfile.getId());
        }

        UserResponse user = userClient.findById(userId);
//        ProfileReadDto base = profileMapper.toReadDto(profile);
        return profileMapper.toReadDto(profile, user.username(), friendIds);
//        return ProfileReadDto.builder()
//                .id(base.id())
//                .username(user.username())
//                .firstname(base.firstname())
//                .lastname(base.lastname())
//                .birthDate(base.birthDate())
//                .bio(base.bio())
//                .friendIds(friendIds)
//                .build();
    }

    @Transactional
    public ProfileReadDto create(ProfileCreateDto profileCreateDto) {
        Profile profile = profileMapper.toEntity(profileCreateDto);
        profile.setUserId(profileCreateDto.userId());
        return profileMapper.toReadDto(profileRepository.save(profile), null, null);
    }

    @Transactional
    @PreAuthorize(
            "@profileAuthorization.isProfileOwner(#profileId, authentication.name)"
    )
    public void editProfile(Integer profileId, ProfileEditDto profileEditDto) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        profile.setFirstname(profileEditDto.firstname());
        profile.setLastname(profileEditDto.lastname());
        profile.setBirthDate(profileEditDto.birthDate());
        profile.setBio(profileEditDto.bio());
    }

    public ProfilePreviewReadDto search(String query) {
        UserResponse user;

        try {
            Integer id = Integer.parseInt(query);
            user = userClient.findById(id);
        } catch (Exception e) {
            user = userClient.findByUsername(query);
        }

        if(user == null) {
            throw new ProfileNotFoundException("Profile not found");
        }

        Profile profile = profileRepository.findByUserId(user.id())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

//        ProfilePreviewReadDto base = profileMapper.toPreview(profile);
//        return new ProfilePreviewReadDto(
//                user.username(),
//                base.firstname(),
//                base.lastname()
//        );

        return profileMapper.toPreview(profile, user.username());
    }
}
