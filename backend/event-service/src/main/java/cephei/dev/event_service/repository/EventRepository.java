package cephei.dev.event_service.repository;

import cephei.dev.event_service.dto.EventPreviewReadDto;
import cephei.dev.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByHostId(Integer host, Pageable pageable);

    Page<Event> findAllByParticipantIdsContaining(Integer participantId, Pageable pageable);
}
