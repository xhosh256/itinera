package cephei.dev.event_service.service;

import cephei.dev.event_service.dto.EventComponentCreateDto;
import cephei.dev.event_service.dto.EventComponentEditDto;
import cephei.dev.event_service.dto.EventComponentReadDto;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.EventComponent;
import cephei.dev.event_service.exception.EventComponentAccessDeniedException;
import cephei.dev.event_service.exception.EventComponentNotFoundException;
import cephei.dev.event_service.exception.EventNotFound;
import cephei.dev.event_service.mapper.EventComponentMapper;
import cephei.dev.event_service.repository.EventComponentRepository;
import cephei.dev.event_service.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventComponentService {

    private final EventComponentRepository eventComponentRepository;
    private final EventRepository eventRepository;
    private final EventComponentMapper eventComponentMapper;

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

    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    @Transactional
    public void editComponent(Long eventId, Long componentId, EventComponentEditDto eventComponentEditDto) {
        EventComponent component = eventComponentRepository.findById(componentId)
                .orElseThrow(() -> new EventComponentNotFoundException("Event component not found"));

        if(!Objects.equals(component.getEvent().getId(), eventId)) {
            throw new EventComponentAccessDeniedException("Component Access Denied");
        }

        component.setStartTime(eventComponentEditDto.startTime());
        component.setEndTime(eventComponentEditDto.endTime());
        component.setDescription(eventComponentEditDto.description());
        component.setAddress(eventComponentEditDto.address());
    }

    @Transactional
    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public void deleteComponent(Long eventId, Long componentId) {
        EventComponent component = eventComponentRepository.findById(componentId)
                .orElseThrow(() -> new EventComponentNotFoundException("Event component not found"));

        if(!Objects.equals(component.getEvent().getId(), eventId)) {
            throw new EventComponentAccessDeniedException("Component Access Denied");
        }

        eventComponentRepository.delete(component);
    }


    @PreAuthorize(
            "@eventAuthorization.isHost(#eventId, authentication.name)"
    )
    public EventComponentReadDto findComponentById(Long eventId, Long componentId) {
        EventComponent component = eventComponentRepository.findById(componentId)
                .orElseThrow(() -> new EventComponentNotFoundException("Event component not found"));

        if(!Objects.equals(component.getEvent().getId(), eventId)) {
            throw new EventComponentAccessDeniedException("Component Access Denied");
        }

        return eventComponentMapper.toReadDto(component);
    }
}
