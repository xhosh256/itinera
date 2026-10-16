package cephei.dev.event_service.service;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.*;
import cephei.dev.event_service.entity.*;
import cephei.dev.event_service.exception.AlreadyTakingPartException;
import cephei.dev.event_service.exception.EventAccessDeniedException;
import cephei.dev.event_service.exception.EventCapacityExceededException;
import cephei.dev.event_service.exception.EventNotFound;
import cephei.dev.event_service.mapper.EventComponentMapper;
import cephei.dev.event_service.mapper.EventMapper;
import cephei.dev.event_service.repository.EventRepository;
import cephei.dev.event_service.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PreAuthorize(
            "@eventAuthorization.hasAccess(#id, authentication.name)"
    )
    public EventReadDto findById(Long id) {
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

    public Page<EventPreviewReadDto> findMy(String username, Pageable pageable) {

        UserResponse host = userClient.findByUsername(username);
        return eventRepository.findAllByHostId(host.id(), pageable)
                .map(e -> new EventPreviewReadDto(
                        e.getId(),
                        e.getName(),
                        e.getCapacity(),
                        e.getJoined(),
                        e.getVisibility()
                ));
    }

    public Page<EventPreviewReadDto> findJoined(String username, Pageable pageable) {

        UserResponse participant = userClient.findByUsername(username);
        return eventRepository.findAllByParticipantIdsContaining(participant.id(), pageable)
                .map(e -> new EventPreviewReadDto(
                        e.getId(),
                        e.getName(),
                        e.getCapacity(),
                        e.getJoined(),
                        e.getVisibility()
                ));
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public void deleteEvent(Long eventId) {
        eventRepository.delete(
                eventRepository.findById(eventId)
                        .orElseThrow(() -> new EventNotFound("Event not found"))
        );
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public void editEvent(Long eventId, EditEventDto editEventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        event.setName(editEventDto.name());
        event.setCapacity(editEventDto.capacity());
        event.setVisibility(editEventDto.visibility());
    }
}
