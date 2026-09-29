package cephei.dev.event_service.controller.api.v1;

import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationRestController {

    private final InvitationService invitationService;

    @GetMapping
    public Page<InvitationReadDto> findAll(
            @AuthenticationPrincipal String username,
            @PageableDefault(size = 3, page = 0) Pageable pageable
    ) {
        return invitationService.findAll(username, pageable);
    }

    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<Void> accept(
            @AuthenticationPrincipal String username,
            @PathVariable Long invitationId
    ) {
        invitationService.accept(username, invitationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invitationId}/decline")
    public ResponseEntity<Void> decline(
            @AuthenticationPrincipal String username,
            @PathVariable Long invitationId
    ) {
        invitationService.decline(username, invitationId);
        return ResponseEntity.ok().build();
    }
}
