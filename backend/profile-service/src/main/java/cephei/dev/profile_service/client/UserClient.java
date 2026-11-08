package cephei.dev.profile_service.client;

import cephei.dev.profile_service.configuration.FeignConfiguration;
import cephei.dev.profile_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "authentication-service",
        configuration = FeignConfiguration.class
)
public interface UserClient {

    @GetMapping("/api/v1/users/username/{username}")
    UserResponse findByUsername(@PathVariable String username);

    @GetMapping("/api/v1/users/id/{userId}")
    UserResponse findById(@PathVariable Integer userId);
}
