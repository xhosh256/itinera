package cephei.dev.profile_service.api.v1;

import cephei.dev.profile_service.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
public class FriendshipRestController {

    private final FriendshipService friendshipService;

    @PostMapping("/{friendId}")
    public ResponseEntity<Void> sendRequest(@AuthenticationPrincipal String username, @PathVariable Integer friendId) {
        friendshipService.sendRequest(username, friendId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
