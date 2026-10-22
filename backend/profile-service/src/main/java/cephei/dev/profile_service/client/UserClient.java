package cephei.dev.profile_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "user-service"
)
public interface UserClient {

}
