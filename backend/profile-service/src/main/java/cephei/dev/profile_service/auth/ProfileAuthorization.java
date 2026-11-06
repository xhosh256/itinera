package cephei.dev.profile_service.auth;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.UserResponse;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.ProfileNotFoundException;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("profileAuthorization")
@RequiredArgsConstructor
public class ProfileAuthorization {

    private final ProfileRepository profileRepository;
    private final UserClient userClient;

    public boolean isProfileOwner(Integer profileId, String username) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        UserResponse userResponse = userClient.findByUsername(username);

        return Objects.equals(profile.getUserId(), userResponse.id());
    }
}
