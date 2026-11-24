package cephei.dev.event_service.client;

import cephei.dev.event_service.configuration.FeignConfiguration;
import cephei.dev.event_service.dto.ParticipantPreview;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "profile-service",
        configuration = FeignConfiguration.class
)
public interface ProfileClient {

    @GetMapping("api/v1/profiles/{userId}")
    ParticipantPreview findByUserId(@PathVariable Integer userId);
}
