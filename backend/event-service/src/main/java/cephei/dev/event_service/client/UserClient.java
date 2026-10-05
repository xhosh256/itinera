package cephei.dev.event_service.client;

import cephei.dev.event_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "authentication-service"
)
public interface UserClient {

    @GetMapping("api/v1/users/{username}")
    UserResponse findByUsername(@PathVariable String username);
}
