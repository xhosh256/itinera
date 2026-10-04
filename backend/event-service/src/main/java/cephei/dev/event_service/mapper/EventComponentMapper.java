package cephei.dev.event_service.mapper;

import cephei.dev.event_service.dto.EventComponentCreateDto;
import cephei.dev.event_service.dto.EventComponentReadDto;
import cephei.dev.event_service.entity.EventComponent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventComponentMapper {

    EventComponent toEntity(EventComponentCreateDto createDto);

    EventComponentReadDto toReadDto(EventComponent component);
}
