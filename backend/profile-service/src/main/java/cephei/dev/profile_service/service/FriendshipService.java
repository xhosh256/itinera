package cephei.dev.profile_service.service;

import cephei.dev.profile_service.client.UserClient;
import cephei.dev.profile_service.dto.FriendshipReadDto;
import cephei.dev.profile_service.dto.UserResponse;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import cephei.dev.profile_service.exception.*;
import cephei.dev.profile_service.mapper.FriendshipMapper;
import cephei.dev.profile_service.mapper.ProfileMapper;
import cephei.dev.profile_service.repository.FriendshipRepository;
import cephei.dev.profile_service.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendshipService {

    private final ProfileRepository profileRepository;
    private final UserClient userClient;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final ProfileMapper profileMapper;

    @Transactional
    public void sendRequest(String username, Integer friendId) {
        UserResponse userResponse = userClient.findByUsername(username);

        if(Objects.equals(userResponse.id(), friendId)) {
            throw new SelfRequestException("You cant send self friendship request");
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

    @PreAuthorize(
        "@friendshipAuthorization.doesRequestBelongsToMe(#myId, authentication.name)"
    )
    public List<FriendshipReadDto> getAllRequests(Integer myId) {
        List<Friendship> requests = friendshipRepository
                .findAllFriendRequests(myId, FriendshipStatus.PENDING);

        return requests.stream().map((friendship) -> {
            FriendshipReadDto base = friendshipMapper.toReadDto(friendship);

            return FriendshipReadDto.builder()
                    .id(base.id())
                    .user(profileMapper.toReadDto(friendship.getUser()))
                    .friend(profileMapper.toReadDto(friendship.getFriend()))
                    .status(base.status())
                    .build();
        }).toList();
    }

    @Transactional
    @PreAuthorize(
            "@friendshipAuthorization.doesFriendshipBelongsToMe(#friendshipId, authentication.name)"
    )
    public void accept(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFound("Friendship not found"));

        if(friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new RequestAlreadyProceed("Request already proceed");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
    }

    @Transactional
    @PreAuthorize(
            "@friendshipAuthorization.doesFriendshipBelongsToMe(#friendshipId, authentication.name)"
    )
    public void decline(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new FriendshipNotFound("Friendship not found"));

        if(friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new RequestAlreadyProceed("Request already proceed");
        }

        friendship.setStatus(FriendshipStatus.DECLINED);
    }
}
