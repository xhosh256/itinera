package cephei.dev.event_service.mapper;

import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.dto.OutcomingInvitationReadDto;
import cephei.dev.event_service.entity.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    @Mapping(target = "eventName", source = "event.name")
    @Mapping(target = "visibility", source = "event.visibility")
    InvitationReadDto toReadDto(Invitation invitation);

    default OutcomingInvitationReadDto toInvitationReadDto(Invitation i, String username) {
        return new OutcomingInvitationReadDto(
                i.getId(),
                i.getEvent().getName(),
                username,
                i.getEvent().getVisibility(),
                i.getStatus()
        );
    }
}
