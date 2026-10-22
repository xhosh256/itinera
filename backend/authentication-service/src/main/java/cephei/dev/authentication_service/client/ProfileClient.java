package cephei.dev.authentication_service.client;

import cephei.dev.authentication_service.dto.ProfileCreateRequest;
import cephei.dev.authentication_service.dto.ProfileCreateResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "profile-service"
)
public interface ProfileClient {

    @PostMapping
    ProfileCreateResponse create(ProfileCreateRequest profileCreateRequest);
}
