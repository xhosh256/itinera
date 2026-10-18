package cephei.dev.event_service.controller.api.v1;

import cephei.dev.event_service.dto.*;
import cephei.dev.event_service.entity.EventComponent;
import cephei.dev.event_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;

    @GetMapping("/my")
    public Page<EventPreviewReadDto> findMy(
            @AuthenticationPrincipal String username,
            @PageableDefault(size = 5, page = 0) Pageable pageable
            ) {
        return eventService.findMy(username, pageable);
    }

    @GetMapping("/joined")
    public Page<EventPreviewReadDto> findJoined(
            @AuthenticationPrincipal String username,
            @PageableDefault(size = 5, page = 0) Pageable pageable
    ) {
        return eventService.findJoined(username, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventReadDto create(
            @AuthenticationPrincipal String username,
            @RequestBody EventCreateRequest eventCreateRequest
    ) {
        return eventService.create(username, eventCreateRequest);
    }

    @GetMapping("/{eventId}")
    public EventReadDto findById(
            @PathVariable("eventId") Long id
    ) {
        return eventService.findById(id);
    }

    @GetMapping("/search")
    public Page<EventPreviewReadDto> findByName(
        @RequestParam("name") String name,
        @PageableDefault(size=5, page=0) Pageable pageable
    ) {
        return eventService.findByName(name, pageable);
    }

    @PostMapping("/{eventId}/joins")
    public ResponseEntity<Void> join(
            @AuthenticationPrincipal String username,
            @PathVariable("eventId") Long id
    ) {
        eventService.join(username, id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{eventId}/invitations/{invitedUserId}")
    public ResponseEntity<Void> invite(
            @PathVariable("eventId") Long eventId,
            @PathVariable("invitedUserId") Integer invitedUserId
    ) {
        eventService.invite(eventId, invitedUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable Long eventId
    ) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Void> editEvent(
            @PathVariable("eventId") Long eventId,
            @RequestBody EditEventDto editEventDto
    ) {
        eventService.editEvent(eventId, editEventDto);
        return ResponseEntity.ok().build();
    }
}
