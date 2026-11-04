package cephei.dev.profile_service.mapper;

import cephei.dev.profile_service.dto.FriendReadDto;
import cephei.dev.profile_service.dto.FriendshipReadDto;
import cephei.dev.profile_service.entity.Friendship;
import cephei.dev.profile_service.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FriendshipMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "friend", ignore = true)
    FriendshipReadDto toReadDto(Friendship friendship);
}
