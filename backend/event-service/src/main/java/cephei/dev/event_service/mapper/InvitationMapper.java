package cephei.dev.event_service.mapper;

import cephei.dev.event_service.dto.InvitationReadDto;
import cephei.dev.event_service.entity.Invitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvitationMapper {

    InvitationReadDto toReadDto(Invitation invitation);
}
