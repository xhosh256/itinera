package cephei.dev.event_service.client;

import cephei.dev.event_service.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "authentication-service",
        url = "http://localhost:8082/api/v1/users"
)
public interface UserClient {

    @GetMapping("/{username}")
    UserResponse findByUsername(@PathVariable String username);
}
