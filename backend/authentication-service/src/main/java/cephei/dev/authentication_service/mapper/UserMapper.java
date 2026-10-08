package cephei.dev.authentication_service.mapper;


import cephei.dev.authentication_service.dto.AuthMe;
import cephei.dev.authentication_service.dto.RegisterRequest;
import cephei.dev.authentication_service.dto.UserClientResponse;
import cephei.dev.authentication_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequest registerDto);
    UserClientResponse toClientResponse(User user);

    AuthMe toAuthMe(User user);
}