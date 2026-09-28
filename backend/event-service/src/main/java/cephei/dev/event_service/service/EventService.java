package cephei.dev.event_service.service;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.EventCreateRequest;
import cephei.dev.event_service.dto.EventReadDto;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.exception.EventNotFound;
import cephei.dev.event_service.mapper.EventMapper;
import cephei.dev.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final UserClient userClient;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    @Transactional
    public EventReadDto create(String username, EventCreateRequest eventCreateRequest) {
        UserResponse user = userClient.findByUsername(username);
        Event event = eventMapper.toEntity(eventCreateRequest);
        event.setHostId(user.id());
        event.getParticipantIds().add(user.id());
        return eventMapper.toReadDto(eventRepository.save(event));
    }

    public EventReadDto findById(String username, Long id) {
        UserResponse user = userClient.findByUsername(username);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        return eventMapper.toReadDto(event);
    }
}
