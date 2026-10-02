package cephei.dev.event_service.unit.event;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.Invitation;
import cephei.dev.event_service.entity.Visibility;
import cephei.dev.event_service.exception.AlreadyTakingPartException;
import cephei.dev.event_service.exception.EventAccessDeniedException;
import cephei.dev.event_service.exception.EventCapacityExceededException;
import cephei.dev.event_service.repository.EventRepository;
import cephei.dev.event_service.repository.InvitationRepository;
import cephei.dev.event_service.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceApplicationTests {

	@Mock
	EventRepository eventRepository;
	@Mock
	UserClient userClient;
	@Mock
	InvitationRepository invitationRepository;

	@InjectMocks
	EventService eventService;

	@Test
	void invite() {
		// Arrange
		String hostUsername = "host";
		Long eventId = 1L;
		Integer invitedUserId = 2;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(2)
				.visibility(Visibility.PRIVATE)
				.build();
		UserResponse host = new UserResponse(1);

		when(eventRepository.findById(eventId))
				.thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(hostUsername))
				.thenReturn(host);

		// Act
		eventService.invite(hostUsername, eventId, invitedUserId);

		// Assert
		verify(eventRepository).findById(eventId);
		verify(userClient).findByUsername(hostUsername);
		verify(invitationRepository).save(any(Invitation.class));
	}

	@Test
	void inviteAlreadyParticipant() {
		// Arrange
		String hostUsername = "host";
		Long eventId = 1L;
		Integer invitedUserId = 2;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(2)
				.visibility(Visibility.PRIVATE)
				.build();
		eventEntity.getParticipantIds().add(2);
		UserResponse host = new UserResponse(1);

		when(eventRepository.findById(eventId))
				.thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(hostUsername))
				.thenReturn(host);

		// Act
		assertThrows(AlreadyTakingPartException.class,
				() -> eventService.invite(hostUsername, eventId, invitedUserId));

		// Assert
		verify(eventRepository).findById(eventId);
		verify(userClient).findByUsername(hostUsername);
		verify(invitationRepository, never()).save(any(Invitation.class));
	}

	@Test
	void inviteNotHostSend() {
		// Arrange
		String hostUsername = "host";
		Long eventId = 1L;
		Integer invitedUserId = 2;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(2)
				.visibility(Visibility.PRIVATE)
				.build();
		UserResponse notTheHost = new UserResponse(67);

		when(eventRepository.findById(eventId))
				.thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(hostUsername))
				.thenReturn(notTheHost);

		// Act
		assertThrows(EventAccessDeniedException.class,
				() -> eventService.invite(hostUsername, eventId, invitedUserId));

		// Assert
		verify(eventRepository).findById(eventId);
		verify(userClient).findByUsername(hostUsername);
		verify(invitationRepository, never()).save(any(Invitation.class));
	}

	@Test
	void inviteCapacityExceeded() {
		// Arrange
		String hostUsername = "host";
		Long eventId = 1L;
		Integer invitedUserId = 2;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(2)
				.visibility(Visibility.PRIVATE)
				.build();
		eventEntity.getParticipantIds().add(1);
		eventEntity.getParticipantIds().add(67);

		UserResponse host = new UserResponse(1);

		when(eventRepository.findById(eventId))
				.thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(hostUsername))
				.thenReturn(host);

		// Act
		assertThrows(EventCapacityExceededException.class,
				() -> eventService.invite(hostUsername, eventId, invitedUserId));

		// Assert
		verify(eventRepository).findById(eventId);
		verify(userClient).findByUsername(hostUsername);
		verify(invitationRepository, never()).save(any(Invitation.class));
	}


	@Test
	void join() {
		// Arrange
		String joinUsername = "someuser";
		Long eventId = 1L;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(3)
				.visibility(Visibility.PUBLIC)
				.build();
		eventEntity.getParticipantIds().add(1);
		UserResponse userResponse = new UserResponse(2);

		when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(joinUsername)).thenReturn(userResponse);

		// Act
		eventService.join(joinUsername, eventId);

		// Assert
		assertTrue(eventEntity.containsParticipant(userResponse.id()));

		verify(eventRepository).findById(eventId);
		verify(userClient).findByUsername(joinUsername);
	}

	@Test
	void joinPrivate() {
		// Arrange
		String joinUsername = "someuser";
		Long eventId = 1L;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(3)
				.visibility(Visibility.PRIVATE)
				.build();
		eventEntity.getParticipantIds().add(1);

		when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
		// Act & Assert
		assertThrows(EventAccessDeniedException.class, ()-> eventService.join(joinUsername, eventId));
		verify(eventRepository).findById(eventId);
	}

	@Test
	void alreadyJoined() {
		// Arrange
		String joinUsername = "someuser";
		Long eventId = 1L;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(3)
				.visibility(Visibility.PUBLIC)
				.build();
		eventEntity.getParticipantIds().add(1);
		UserResponse userResponse = new UserResponse(2);

		when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(joinUsername)).thenReturn(userResponse);

		// Act & Assert
		eventService.join(joinUsername, eventId);
		assertThrows(AlreadyTakingPartException.class, () -> eventService.join(joinUsername, eventId));
		verify(eventRepository, times(2)).findById(eventId);
		verify(userClient, times(2)).findByUsername(joinUsername);
	}

	@Test
	void joinCapacityExceeded() {
		String joinUsername1 = "someuser";
		String joinUsername2 = "anotherUser";
		Long eventId = 1L;

		Event eventEntity = Event.builder()
				.id(eventId)
				.hostId(1)
				.name("Event 1")
				.capacity(2)
				.visibility(Visibility.PUBLIC)
				.build();
		eventEntity.getParticipantIds().add(1);
		UserResponse userResponse1 = new UserResponse(2);
		UserResponse userResponse2 = new UserResponse(3);

		when(eventRepository.findById(1L)).thenReturn(Optional.of(eventEntity));
		when(userClient.findByUsername(joinUsername1)).thenReturn(userResponse1);
		when(userClient.findByUsername(joinUsername2)).thenReturn(userResponse2);

		// Act & Assert
		eventService.join(joinUsername1, eventId);
		assertThrows(EventCapacityExceededException.class, () -> eventService.join(joinUsername2, eventId));

		verify(eventRepository, times(2)).findById(eventId);
		verify(userClient).findByUsername(joinUsername1);
		verify(userClient).findByUsername(joinUsername2);
	}
}
