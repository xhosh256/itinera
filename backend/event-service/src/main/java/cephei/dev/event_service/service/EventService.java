package cephei.dev.event_service.service;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.*;
import cephei.dev.event_service.entity.*;
import cephei.dev.event_service.exception.*;
import cephei.dev.event_service.mapper.EventComponentMapper;
import cephei.dev.event_service.mapper.EventMapper;
import cephei.dev.event_service.repository.EventComponentRepository;
import cephei.dev.event_service.repository.EventRepository;
import cephei.dev.event_service.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final UserClient userClient;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;
    private final InvitationRepository invitationRepository;

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

        Optional<Invitation> maybeInvitation = invitationRepository.findByEventIdAndUserId(eventId, invitedUserId);

        if(maybeInvitation.isPresent()) {
            throw new InvitationRequestAlreadySent("Request already sent");
        }

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

    public Page<EventPreviewReadDto> findCreated(Integer userId, Pageable pageable) {
        Page<Event> base = eventRepository.findAllByHostId(userId, pageable);

        return base.map(e -> {
            UserResponse host = userClient.findById(e.getHostId());

            return EventPreviewReadDto.builder()
                    .id(e.getId())
                    .hostUsername(host.username())
                    .name(e.getName())
                    .capacity(e.getCapacity())
                    .joined(e.getJoined())
                    .visibility(e.getVisibility())
                    .build();
        });
    }

    public Page<EventPreviewReadDto> findJoined(Integer userId, Pageable pageable) {
        Page<Event> base = eventRepository.findAllByParticipantIdsContaining(userId, pageable);

        return base.map(e -> {
            UserResponse host = userClient.findById(e.getHostId());

            return EventPreviewReadDto.builder()
                    .id(e.getId())
                    .hostUsername(host.username())
                    .name(e.getName())
                    .capacity(e.getCapacity())
                    .joined(e.getJoined())
                    .visibility(e.getVisibility())
                    .build();
        });
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

    public Page<EventPreviewReadDto> findByName(String name, Pageable pageable) {
        Page<Event> base = eventRepository.findByNameContainingIgnoreCase(name, pageable);


        return base.map(e -> {
            UserResponse host = userClient.findById(e.getHostId());

            return EventPreviewReadDto.builder()
                    .id(e.getId())
                    .hostUsername(host.username())
                    .name(e.getName())
                    .capacity(e.getCapacity())
                    .joined(e.getJoined())
                    .visibility(e.getVisibility())
                    .build();
        });
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public void kick(Long eventId, Integer userId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        if (!event.getParticipantIds().contains(userId)) {
            throw new UserNotInEvent("User is not taking a part of your event");
        }

        if (event.getHostId().equals(userId)) {
            throw new HostSelfRemoveFromEventException("Host cant kick himself");
        }

        event.getParticipantIds().remove(userId);
    }

    @Transactional
    public void leave(String username, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFound("Event not found"));

        UserResponse user = userClient.findByUsername(username);
        if (!event.getParticipantIds().contains(user.id())) {
            throw new UserNotInEvent("User is not taking a part of your event");
        }

        if(event.getHostId().equals(user.id())) {
            throw new HostSelfRemoveFromEventException("Host cant leave from event");
        }

        event.getParticipantIds().remove(user.id());
    }

    public Page<EventPreviewReadDto> findUpcomingEvents() {
        Page<Event> upcomingEvents = eventRepository
                .findUpcomingEvents(LocalDateTime.now(), PageRequest.of(0, 3));
        return upcomingEvents.map(e -> {
            UserResponse host = userClient.findById(e.getHostId());

            return EventPreviewReadDto.builder()
                    .id(e.getId())
                    .hostUsername(host.username())
                    .name(e.getName())
                    .capacity(e.getCapacity())
                    .joined(e.getJoined())
                    .visibility(e.getVisibility())
                    .build();
        });
    }
}
