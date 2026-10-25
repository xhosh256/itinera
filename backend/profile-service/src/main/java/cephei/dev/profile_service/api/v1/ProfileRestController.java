package cephei.dev.profile_service.api.v1;

import cephei.dev.profile_service.dto.ProfileCreateDto;
import cephei.dev.profile_service.dto.ProfileReadDto;
import cephei.dev.profile_service.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileRestController {


    private final ProfileService profileService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProfileReadDto> create(
            @RequestBody ProfileCreateDto profileCreateDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.create(profileCreateDto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileReadDto> findByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(profileService.findByUserId(userId));
    }
}
