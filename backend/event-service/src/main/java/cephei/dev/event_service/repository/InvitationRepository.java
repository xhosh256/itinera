package cephei.dev.event_service.repository;

import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.entity.Invitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Page<Invitation> findByInvitedUserId(Integer invitedUserId, Pageable pageable);


    @Query("""
        select i from Invitation i
        where i.event.id = :eventId 
        and i.invitedUserId = :invitedUserId
    """)
    Optional<Invitation> findByEventIdAndUserId(Long eventId, Integer invitedUserId);
}
