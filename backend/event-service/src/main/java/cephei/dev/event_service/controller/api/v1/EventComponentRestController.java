package cephei.dev.event_service.controller.api.v1;

import cephei.dev.event_service.dto.EventComponentCreateDto;
import cephei.dev.event_service.dto.EventComponentEditDto;
import cephei.dev.event_service.dto.EventComponentReadDto;
import cephei.dev.event_service.service.EventComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events/{eventId}/components")
@RequiredArgsConstructor
public class EventComponentRestController {

    private final EventComponentService eventComponentService;

    @GetMapping("/{componentId}")
    public EventComponentReadDto findComponentById(
            @PathVariable Long eventId,
            @PathVariable Long componentId
    ) {
        return eventComponentService.findComponentById(eventId, componentId);
    }

    @PostMapping
    public EventComponentReadDto addComponent(
            @PathVariable("eventId") Long eventId,
            @RequestBody EventComponentCreateDto eventComponentCreateDto
    ) {
        return eventComponentService.addComponent(eventId, eventComponentCreateDto);
    }

    @PutMapping("/{componentId}")
    public ResponseEntity<Void> editComponent(
            @PathVariable Long eventId,
            @PathVariable Long componentId,
            @RequestBody EventComponentEditDto eventComponentEditDto
    ) {
        eventComponentService.editComponent(eventId, componentId, eventComponentEditDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{componentId}")
    public ResponseEntity<Void> deleteComponent(
            @PathVariable Long eventId,
            @PathVariable Long componentId
    ) {
        eventComponentService.deleteComponent(eventId, componentId);
        return ResponseEntity.ok().build();
    }
}
