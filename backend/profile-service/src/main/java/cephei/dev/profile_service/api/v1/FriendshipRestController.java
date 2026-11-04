package cephei.dev.profile_service.api.v1;

import cephei.dev.profile_service.dto.FriendshipReadDto;
import cephei.dev.profile_service.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/requests/{myId}")
    public List<FriendshipReadDto> getAllRequests(@PathVariable Integer myId) {
        return friendshipService.getAllRequests(myId);
    }

    @PostMapping("/requests/accept/{friendshipId}")
    public ResponseEntity<Void> acceptFriendship(@PathVariable Long friendshipId) {
        friendshipService.accept(friendshipId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/requests/decline/{friendshipId}")
    public ResponseEntity<Void> declineFriendship(@PathVariable Long friendshipId) {
        friendshipService.decline(friendshipId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
