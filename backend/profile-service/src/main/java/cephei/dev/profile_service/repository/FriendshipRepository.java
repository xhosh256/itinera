package cephei.dev.profile_service.repository;

import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.FriendshipStatus;
import cephei.dev.profile_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("""
        select f from Friendship f
        where f.status = :status and
        (f.friend = :profile or f.user = :profile)
    """)
    List<Friendship> findAllByProfileAndStatus(FriendshipStatus status, Profile profile);

    @Query("""
        select f from Friendship f
        where (f.user.userId = :userId and f.friend.userId = :friendId)
        or (f.friend.userId = :userId and f.user.userId = :friendId)
    """)
    Optional<Friendship> findFriendshipByFriendIds(Integer userId, Integer friendId);

    Optional<Friendship> findByUser_UserIdAndFriend_UserId(Integer id, Integer friendId);
}
