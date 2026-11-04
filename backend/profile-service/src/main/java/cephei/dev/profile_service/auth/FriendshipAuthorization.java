package cephei.dev.profile_service.auth;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.UserResponse;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.exception.FriendshipNotFound;
import cephei.dev.profile_service.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("friendshipAuthorization")
@RequiredArgsConstructor
public class FriendshipAuthorization {

    private final UserClient userClient;
    private final FriendshipRepository friendshipRepository;

    public boolean doesRequestBelongsToMe(Integer myId, String username) {
        UserResponse user = userClient.findByUsername(username);

        return Objects.equals(user.id(), myId);
    }

    public boolean doesFriendshipBelongsToMe(Long friendshipId, String username) {
        UserResponse user = userClient.findByUsername(username);
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFound("Friendship not found"));

        return Objects.equals(friendship.getFriend().getUserId(), user.id());
    }
}
