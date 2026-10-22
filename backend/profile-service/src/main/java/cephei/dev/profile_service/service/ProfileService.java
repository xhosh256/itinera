package cephei.dev.profile_service.service;

import cephei.dev.profile_service.dto.ProfileCreateDto;
import cephei.dev.profile_service.dto.ProfileReadDto;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.ProfileNotFoundException;
import cephei.dev.profile_service.mapper.ProfileMapper;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileReadDto findById(Integer profileId) {
        return profileRepository.findById(profileId)
                .map(profileMapper::toReadDto)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
    }

    @Transactional
    public ProfileReadDto create(ProfileCreateDto profileCreateDto) {
        Profile profile = profileMapper.toEntity(profileCreateDto);
        profile.setUserId(profileCreateDto.userId());
        return profileMapper.toReadDto(profileRepository.save(profile));
    }
}
