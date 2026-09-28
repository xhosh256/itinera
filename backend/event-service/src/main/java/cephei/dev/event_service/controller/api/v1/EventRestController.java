package cephei.dev.event_service.controller.api.v1;

import cephei.dev.event_service.dto.EventCreateRequest;
import cephei.dev.event_service.dto.EventReadDto;
import cephei.dev.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventReadDto create(
            @AuthenticationPrincipal String username,
            @RequestBody EventCreateRequest eventCreateRequest
    ) {
        return eventService.create(username, eventCreateRequest);
    }

    @GetMapping("/{id}")
    public EventReadDto findById(
            @AuthenticationPrincipal String username,
            @PathVariable Long id
    ) {
        return eventService.findById(username, id);
    }


}
