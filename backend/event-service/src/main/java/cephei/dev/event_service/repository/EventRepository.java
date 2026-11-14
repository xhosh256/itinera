package cephei.dev.event_service.repository;

import cephei.dev.event_service.dto.MyEventsStatistics;
import cephei.dev.event_service.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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


    @Query("""
        select 
            (select count(*) from Event e where :id member of e.participantIds) 
                as totalEvents,
            (select count(*) from Event e where e.hostId = :id ) 
                as totalHostedEvents
    """)
    MyEventsStatistics getTotal(Integer id);

    @Query("""
        select distinct e from Event e
        join EventComponent ec on ec.event.id = e.id
        where (:id member of e.participantIds and 
            ec.startTime between :now and :nextWeek)
    """)
    List<Event> getWeeklyEvents(Integer id, LocalDateTime now, LocalDateTime nextWeek, Pageable pageable);


    @Query("""
        select e from Event e
        join EventComponent ec on e.id = ec.event.id
        where :id member of e.participantIds and ec.startTime > :now
        group by e
        order by MIN(ec.startTime) asc
    """)
    List<Event> getNextEvent(LocalDateTime now, Integer id, Pageable pageable);
}
