package cephei.dev.event_service.mapper;

import cephei.dev.event_service.dto.EventCreateRequest;
import cephei.dev.event_service.dto.EventPreviewReadDto;
import cephei.dev.event_service.dto.EventReadDto;
import cephei.dev.event_service.entity.Event;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEntity(EventCreateRequest eventCreateRequest);

    EventReadDto toReadDto(Event event);

    default EventPreviewReadDto toPreviewReadDto(Event e) {
        return new EventPreviewReadDto(
                e.getId(),
                e.getName(),
                e.getCapacity(),
                e.getJoined(),
                e.getVisibility()
        );
    }
}
