package cephei.dev.event_service.auth;

import cephei.dev.event_service.client.UserClient;
import cephei.dev.event_service.dto.UserResponse;
import cephei.dev.event_service.entity.Invitation;
import cephei.dev.event_service.exception.InvitationNotFoundException;
import cephei.dev.event_service.repository.InvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("invitationAuthorization")
@RequiredArgsConstructor
public class InvitationAuthorization {

    private final InvitationRepository invitationRepository;
    private final UserClient userClient;

    public boolean isInvited(Long invitationId, String invitedUsername) {
        UserResponse invitedUser = userClient.findByUsername(invitedUsername);
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new InvitationNotFoundException("Invitation not found"));

        return invitation.getInvitedUserId().equals(invitedUser.id());
    }
}
