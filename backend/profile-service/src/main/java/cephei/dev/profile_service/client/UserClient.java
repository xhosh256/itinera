package cephei.dev.profile_service.client;

import cephei.dev.profile_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "user-service"
)
public interface UserClient {

    @GetMapping
    public UserResponse findByUsername(String username);
}
