package cephei.dev.authentication_service.service;

import cephei.dev.authentication_service.dto.*;
import cephei.dev.authentication_service.entity.Role;
import cephei.dev.authentication_service.entity.User;
import cephei.dev.authentication_service.exception.InvalidUsernameOrPassword;
import cephei.dev.authentication_service.mapper.UserMapper;
import cephei.dev.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public RegisterResponse register(RegisterRequest registerDto) {

        User user = userMapper.toEntity(registerDto);
        // создание профиля

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        System.out.print(user);

        userRepository.save(user);
        // сохраняем профиль

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", List.of(user.getRole()));

        return new RegisterResponse(jwtService.generateToken(claims, user.getUsername()));
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
        ));

        if(!auth.isAuthenticated()) {
            throw new InvalidUsernameOrPassword("Invalid username or password");
        }

        UserDetails ud = (UserDetails) auth.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", ud.getAuthorities());

        return new LoginResponse(jwtService.generateToken(claims, ud.getUsername()));
    }
}
