package cephei.dev.event_service.service;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.*;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.EventComponent;
import cephei.dev.event_service.entity.Invitation;
import cephei.dev.event_service.entity.InvitationStatus;
import cephei.dev.event_service.exception.AlreadyTakingPartException;
import cephei.dev.event_service.exception.EventAccessDeniedException;
import cephei.dev.event_service.exception.EventCapacityExceededException;
import cephei.dev.event_service.exception.EventNotFound;
import cephei.dev.event_service.mapper.EventComponentMapper;
import cephei.dev.event_service.mapper.EventMapper;
import cephei.dev.event_service.repository.EventRepository;
import cephei.dev.event_service.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final InvitationRepository invitationRepository;
    private final EventComponentMapper eventComponentMapper;

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

    @Transactional
    public void join(String username, Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFound("Event not found"));
        if(!event.isPublic()) throw new EventAccessDeniedException("Private event access denied");

        UserResponse user = userClient.findByUsername(username);
        if(event.containsParticipant(user.id()))
            throw new AlreadyTakingPartException("User is already taking a part of this event");

        if(event.getCapacity() < event.getParticipantIds().size() + 1) {
            throw new EventCapacityExceededException("Event capacity exceeded");
        }

        event.getParticipantIds().add(user.id());
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public void invite(Long eventId, Integer invitedUserId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));
        if(event.containsParticipant(invitedUserId))
            throw new AlreadyTakingPartException("User is already taking a part of this event");

        if(event.getCapacity() < event.getParticipantIds().size() + 1) {
            throw new EventCapacityExceededException("Event capacity exceeded");
        }

        Invitation invitation = Invitation.builder()
                .event(event)
                .invitedUserId(invitedUserId)
                .status(InvitationStatus.PENDING)
                .build();
        invitationRepository.save(invitation);
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public EventComponentReadDto addComponent(Long eventId, EventComponentCreateDto eventComponentCreateDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        EventComponent component = eventComponentMapper.toEntity(eventComponentCreateDto);
        event.addComponent(component);
        return eventComponentMapper.toReadDto(component);
    }
}
