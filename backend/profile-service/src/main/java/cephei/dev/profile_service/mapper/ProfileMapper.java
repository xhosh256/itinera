package cephei.dev.profile_service.mapper;

import cephei.dev.profile_service.dto.ProfileCreateDto;
import cephei.dev.profile_service.dto.ProfileReadDto;
import cephei.dev.profile_service.entity.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileReadDto toReadDto(Profile profile);

    Profile toEntity(ProfileCreateDto profileCreateDto);
}
