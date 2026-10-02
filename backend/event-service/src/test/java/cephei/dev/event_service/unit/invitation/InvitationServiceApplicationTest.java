package cephei.dev.event_service.unit.invitation;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.Invitation;
import cephei.dev.event_service.entity.InvitationStatus;
import cephei.dev.event_service.entity.Visibility;
import cephei.dev.event_service.exception.EventCapacityExceededException;
import cephei.dev.event_service.exception.InvitationAccessDeniedException;
import cephei.dev.event_service.repository.InvitationRepository;
import cephei.dev.event_service.service.InvitationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceApplicationTest {

    @Mock
    UserClient userClient;
    @Mock
    InvitationRepository invitationRepository;

    @InjectMocks
    InvitationService invitationService;

    @Test
    public void accept() {
        Integer userId = 1;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(userId)
                .status(InvitationStatus.PENDING).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        invitationService.accept(username, invitationId);

        //Assert
        assertEquals(InvitationStatus.ACCEPTED, invitation.getStatus());
        assertTrue(invitation.getEvent().getParticipantIds().contains(userId));
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void acceptInvitationAccessDenies() {
        Integer userId = 2;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(1)
                .status(InvitationStatus.PENDING).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        assertThrows(InvitationAccessDeniedException.class,
                () -> invitationService.accept(username, invitationId));

        //Assert
        assertEquals(InvitationStatus.PENDING, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void acceptAlreadyAcceptedOrDeclined() {
        Integer userId = 1;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(userId)
                .status(InvitationStatus.DECLINED).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        invitationService.accept(username, invitationId);

        //Assert
        assertEquals(InvitationStatus.DECLINED, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void acceptCapacityExceeded() {
        Integer userId = 1;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        event.getParticipantIds().add(6752);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(userId)
                .status(InvitationStatus.PENDING).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        assertThrows(EventCapacityExceededException.class,
                () -> invitationService.accept(username, invitationId));

        //Assert
        assertEquals(InvitationStatus.PENDING, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void decline() {
        Integer userId = 1;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(userId)
                .status(InvitationStatus.PENDING).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        invitationService.decline(username, invitationId);

        //Assert
        assertEquals(InvitationStatus.DECLINED, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void declineInvitationAccessDenies() {
        Integer userId = 2;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(1)
                .status(InvitationStatus.PENDING).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        assertThrows(InvitationAccessDeniedException.class,
                () -> invitationService.decline(username, invitationId));

        //Assert
        assertEquals(InvitationStatus.PENDING, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }

    @Test
    public void declineAlreadyAcceptedOrDeclined() {
        Integer userId = 1;
        String username = "user";
        Long invitationId = 1L;
        Long eventId = 1L;

        UserResponse userResponse = new UserResponse(userId);
        Event event = Event.builder().id(eventId)
                .hostId(67)
                .name("Some Event")
                .capacity(2)
                .visibility(Visibility.PRIVATE)
                .build();
        event.getParticipantIds().add(67);
        Invitation invitation = Invitation.builder()
                .id(invitationId)
                .event(event)
                .invitedUserId(userId)
                .status(InvitationStatus.DECLINED).build();
        when(userClient.findByUsername(username)).thenReturn(userResponse);
        when(invitationRepository.findById(invitationId)).thenReturn(Optional.of(invitation));

        // Act
        invitationService.decline(username, invitationId);

        //Assert
        assertEquals(InvitationStatus.DECLINED, invitation.getStatus());
        assertThat(invitation.getEvent().getParticipantIds())
                .doesNotContain(userId);
        verify(userClient).findByUsername(username);
        verify(invitationRepository).findById(invitationId);
    }
}
