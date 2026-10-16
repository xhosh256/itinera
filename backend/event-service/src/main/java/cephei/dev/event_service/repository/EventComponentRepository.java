package cephei.dev.event_service.repository;

import cephei.dev.event_service.entity.EventComponent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventComponentRepository extends JpaRepository<EventComponent, Long> {
}
