package cephei.dev.authentication_service.controller.api.v1;

import cephei.dev.authentication_service.dto.LoginRequest;
import cephei.dev.authentication_service.dto.LoginResponse;
import cephei.dev.authentication_service.dto.RegisterRequest;
import cephei.dev.authentication_service.dto.RegisterResponse;
import cephei.dev.authentication_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest registerDto) {
        return authenticationService.register(registerDto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }
}
