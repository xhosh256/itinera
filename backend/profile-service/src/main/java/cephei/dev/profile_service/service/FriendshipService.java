package cephei.dev.profile_service.service;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.UserResponse;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.AlreadyFriendsException;
import cephei.dev.profile_service.exception.FriendshipRequestAlreadyPendingException;
import cephei.dev.profile_service.exception.ProfileNotFoundException;
import cephei.dev.profile_service.exception.SelfRequestException;
import cephei.dev.profile_service.repository.FriendshipRepository;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

    private final ProfileRepository profileRepository;
    private final UserClient userClient;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public void sendRequest(String username, Integer friendId) {
        UserResponse userResponse = userClient.findByUsername(username);

        if(Objects.equals(userResponse.id(), friendId)) {
            throw new SelfRequestException("Ypu cant send self friendship request");
        }

        Profile user = profileRepository.findByUserId(userResponse.id())
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        Profile friend = profileRepository.findByUserId(friendId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        Optional<Friendship> maybeFriendship = friendshipRepository
                .findFriendshipByFriendIds(userResponse.id(), friendId);

        if(maybeFriendship.isEmpty()) {
            Friendship friendship = Friendship.builder()
                .user(user)
                .friend(friend)
                .status(FriendshipStatus.PENDING)
                .build();
            friendshipRepository.save(friendship);
            return;
        }

        Friendship friendship = maybeFriendship.get();

        if(friendship.getStatus().equals(FriendshipStatus.PENDING)) {
            if(Objects.equals(friendship.getUser().getUserId(), friendId)) {
                friendship.setStatus(FriendshipStatus.ACCEPTED);
            } else {
                throw new FriendshipRequestAlreadyPendingException("Request already sent");
            }
        } else if(friendship.getStatus().equals(FriendshipStatus.ACCEPTED)) {
            throw new AlreadyFriendsException("Already friends");
        } else {
            if(Objects.equals(friendship.getUser().getUserId(), userResponse.id())) {
                friendship.setStatus(FriendshipStatus.PENDING);
            } else {
                friendship.setUser(friend);
                friendship.setFriend(user);
                friendship.setStatus(FriendshipStatus.PENDING);
            }
        }
    }
}
