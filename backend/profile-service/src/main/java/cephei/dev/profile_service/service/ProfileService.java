package cephei.dev.profile_service.service;

import cephei.dev.profile_service.dto.FriendReadDto;
import cephei.dev.profile_service.dto.ProfileCreateDto;
import cephei.dev.profile_service.dto.ProfileReadDto;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.ProfileNotFoundException;
import cephei.dev.profile_service.mapper.ProfileMapper;
import cephei.dev.profile_service.repository.FriendshipRepository;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
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

    public ProfileReadDto findByUserId(Integer userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        ProfileReadDto base = profileMapper.toReadDto(profile);

        List<FriendReadDto> friends = new ArrayList<>();
        Set<Integer> friendIds = new HashSet<>();


        for (Friendship friendship : friendshipRepository.findAllByProfileIdAndStatus(
                FriendshipStatus.ACCEPTED, profile.getId())
        ) {
            Profile friendProfile = Objects.equals(friendship.getUser().getId(), profile.getId())
                    ? friendship.getFriend()
                    : friendship.getUser();

            FriendReadDto friend = FriendReadDto.builder()
                    .id(friendProfile.getId())
                    .firstname(friendProfile.getFirstname())
                    .lastname(friendProfile.getLastname())
                    .build();

            friends.add(friend);
            friendIds.add(friend.id());
        }
        return ProfileReadDto.builder()
                .id(base.id())
                .firstname(base.firstname())
                .lastname(base.lastname())
                .birthDate(base.birthDate())
                .bio(base.bio())
                .friends(friends)
                .friendIds(friendIds)
                .build();
    }

    @Transactional
    public ProfileReadDto create(ProfileCreateDto profileCreateDto) {
        Profile profile = profileMapper.toEntity(profileCreateDto);
        profile.setUserId(profileCreateDto.userId());
        return profileMapper.toReadDto(profileRepository.save(profile));
    }
}
