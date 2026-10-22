package cephei.dev.event_service.repository;

import cephei.dev.event_service.dto.EventPreviewReadDto;
import cephei.dev.event_service.dto.EventReadDto;
import cephei.dev.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.web.PageableDefault;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByHostId(Integer host, Pageable pageable);

    Page<Event> findAllByParticipantIdsContaining(Integer participantId, Pageable pageable);

    Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("""
        SELECT e FROM Event e
        JOIN e.components c
        WHERE e.visibility = 'PUBLIC'
          AND c.startTime > :now
        GROUP BY e
        ORDER BY MIN(c.startTime) ASC
    """)
    Page<Event> findUpcomingEvents(LocalDateTime now, Pageable pageable);
}
