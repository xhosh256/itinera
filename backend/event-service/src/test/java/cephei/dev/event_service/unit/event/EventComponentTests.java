package cephei.dev.event_service.unit.event;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.EventComponentCreateDto;
import cephei.dev.event_service.dto.EventComponentReadDto;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.EventComponent;
import cephei.dev.event_service.entity.Visibility;
import cephei.dev.event_service.exception.EventAccessDeniedException;
import cephei.dev.event_service.mapper.EventComponentMapper;
import cephei.dev.event_service.mapper.EventMapper;
import cephei.dev.event_service.repository.EventRepository;
import cephei.dev.event_service.repository.InvitationRepository;
import cephei.dev.event_service.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventComponentTests {

    @Mock
    private UserClient userClient;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventComponentMapper eventComponentMapper;

    @InjectMocks
    private EventService eventService;

    @Test
    void addComponent() {
        // Arrange
        Long eventId = 1L;
        String username = "bob";
        EventComponentCreateDto componentCreateDto = new EventComponentCreateDto(
                LocalDateTime.of(
                        LocalDate.of(2026, 10, 5),
                        LocalTime.of(10, 15)
                ),
                LocalDateTime.of(
                        LocalDate.of(2026, 10, 5),
                        LocalTime.of(12,0)
                ),
                "abc",
                "Ekaterunburg, Lomonosova st."
        );

        Event event = Event.builder()
                .id(eventId)
                .hostId(1)
                .name("event1")
                .capacity(3)
                .visibility(Visibility.PRIVATE)
                .build();
        UserResponse host = new UserResponse(1);
        EventComponent component = EventComponent.builder()
                .startTime(componentCreateDto.startTime())
                .endTime(componentCreateDto.endTime())
                .description(componentCreateDto.description())
                .address(componentCreateDto.address())
                .build();
        EventComponentReadDto readDto = new EventComponentReadDto(
                1L,
                component.getStartTime(),
                component.getEndTime(),
                component.getDescription(),
                component.getAddress()
        );

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userClient.findByUsername(username)).thenReturn(host);
        when(eventComponentMapper.toEntity(componentCreateDto)).thenReturn(component);
        when(eventComponentMapper.toReadDto(component)).thenReturn(readDto);

        // Act
        EventComponentReadDto result = eventService.addComponent(eventId, username, componentCreateDto);

        // Assert
        assertEquals(1L, result.id());
        verify(eventRepository).findById(eventId);
        verify(userClient).findByUsername(username);
        verify(eventComponentMapper).toEntity(componentCreateDto);
        verify(eventComponentMapper).toReadDto(component);
    }


    @Test
    void addComponentEventAccessDenied() {
        // Arrange
        Long eventId = 1L;
        String username = "bob";
        EventComponentCreateDto componentCreateDto = new EventComponentCreateDto(
                LocalDateTime.of(
                        LocalDate.of(2026, 10, 5),
                        LocalTime.of(10, 15)
                ),
                LocalDateTime.of(
                        LocalDate.of(2026, 10, 5),
                        LocalTime.of(12,0)
                ),
                "abc",
                "Ekaterunburg, Lomonosova st."
        );

        Event event = Event.builder()
                .id(eventId)
                .hostId(2)
                .name("event1")
                .capacity(3)
                .visibility(Visibility.PRIVATE)
                .build();
        UserResponse host = new UserResponse(1);
        EventComponent component = EventComponent.builder()
                .startTime(componentCreateDto.startTime())
                .endTime(componentCreateDto.endTime())
                .description(componentCreateDto.description())
                .address(componentCreateDto.address())
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(userClient.findByUsername(username)).thenReturn(host);

        // Act
        assertThrows(EventAccessDeniedException.class,
                () -> eventService.addComponent(eventId, username, componentCreateDto));

        // Assert
        verify(eventRepository).findById(eventId);
        verify(userClient).findByUsername(username);
        verify(eventComponentMapper, never()).toEntity(componentCreateDto);
        verify(eventComponentMapper, never()).toReadDto(component);
    }
}

