package cephei.dev.event_service.auth;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.Visibility;
import cephei.dev.event_service.exception.EventAccessDeniedException;
import cephei.dev.event_service.exception.EventNotFound;
import cephei.dev.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("eventAuthorization")
@RequiredArgsConstructor
public class EventAuthorization {

    private final EventRepository eventRepository;
    private final UserClient userClient;

    public boolean isHost(Long eventId, String hostUsername) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));
        UserResponse host = userClient.findByUsername(hostUsername);

        return event.getHostId().equals(host.id());
    }

    public boolean hasAccess(Long eventId, String participantUsername) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        if(event.getVisibility() == Visibility.PUBLIC) return true;

        UserResponse participant = userClient.findByUsername(participantUsername);

        if (event.getParticipantIds().contains(participant.id())) return true;

        throw new EventAccessDeniedException("Event access denied");
    }
}
