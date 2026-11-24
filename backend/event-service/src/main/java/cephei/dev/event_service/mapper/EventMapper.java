package cephei.dev.event_service.mapper;

import cephei.dev.event_service.dto.EventCreateRequest;
import cephei.dev.event_service.dto.EventPreviewReadDto;
import cephei.dev.event_service.dto.EventReadDto;
import cephei.dev.event_service.dto.ParticipantPreview;
import cephei.dev.event_service.entity.Event;
import cephei.dev.event_service.entity.EventComponent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    Event toEntity(EventCreateRequest eventCreateRequest);

    EventReadDto toReadDto(Event event, String hostUsername, List<ParticipantPreview> participantPreviews);

    default EventPreviewReadDto toPreviewReadDto(Event e, String username) {
        List<EventComponent> nearestList = e.getComponents();

        EventComponent nearest = nearestList.isEmpty() ? null : nearestList.getFirst();

        return new EventPreviewReadDto(
                e.getId(),
                username,
                e.getName(),
                e.getCapacity(),
                e.getJoined(),
                e.getVisibility(),

                nearest == null ? null : nearest.getAddress(),
                nearest == null ? null : nearest.getStartTime()
        );
    }
}
