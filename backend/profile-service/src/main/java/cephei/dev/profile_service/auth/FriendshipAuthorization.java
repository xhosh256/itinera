package cephei.dev.profile_service.auth;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("friendshipAuthorization")
@RequiredArgsConstructor
public class FriendshipAuthorization {

    private final UserClient userClient;

    public boolean doesRequestBelongsToMe(Integer myId, String username) {
        UserResponse user = userClient.findByUsername(username);

        return Objects.equals(user.id(), myId);
    }
}
