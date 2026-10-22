package cephei.dev.authentication_service.dto;

import lombok.Builder;

@Builder
public record ProfileCreateRequest (
        Integer userId,
        String firstname,
        String lastname
){
}
