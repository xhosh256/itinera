package cephei.dev.event_service.service;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Invitation;
import cephei.dev.event_service.entity.InvitationStatus;
import cephei.dev.event_service.exception.EventCapacityExceededException;
import cephei.dev.event_service.exception.InvitationAccessDeniedException;
import cephei.dev.event_service.exception.InvitationNotFoundException;
import cephei.dev.event_service.mapper.InvitationMapper;
import cephei.dev.event_service.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final InvitationMapper invitationMapper;
    private final UserClient userClient;

    public Page<InvitationReadDto> findAll(String username, Pageable pageable) {
        UserResponse user = userClient.findByUsername(username);
        return invitationRepository.findByInvitedUserId(user.id(), pageable)
                .map(invitationMapper::toReadDto);

    }

    @Transactional
    public void accept(String username, Long invitationId) {
        UserResponse user = userClient.findByUsername(username);
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        if(!Objects.equals(invitation.getInvitedUserId(), user.id())) {
            throw new InvitationAccessDeniedException("Invitation access denied");
        }

        if(!invitation.getStatus().equals(InvitationStatus.PENDING)) {
            return;
        }

        if(invitation.getEvent().getCapacity() < invitation.getEvent().getParticipantIds().size() + 1) {
            throw new EventCapacityExceededException("Event capacity exceeded");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.getEvent().getParticipantIds().add(user.id());
    }

    @Transactional
    public void decline(String username, Long invitationId) {
        UserResponse user = userClient.findByUsername(username);
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        if(!Objects.equals(invitation.getInvitedUserId(), user.id())) {
            throw new InvitationAccessDeniedException("Invitation access denied");
        }

        if(!invitation.getStatus().equals(InvitationStatus.PENDING)) {
            return;
        }

        invitation.setStatus(InvitationStatus.DECLINED);
    }
}
