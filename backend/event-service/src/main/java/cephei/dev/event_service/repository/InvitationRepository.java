package cephei.dev.event_service.repository;

import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.entity.Invitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Page<Invitation> findByInvitedUserId(Integer invitedUserId, Pageable pageable);
}
