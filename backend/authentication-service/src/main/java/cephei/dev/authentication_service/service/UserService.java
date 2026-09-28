package cephei.dev.authentication_service.service;

import cephei.dev.authentication_service.dto.UserDetailsImpl;
import cephei.dev.authentication_service.dto.UserReadDto;
import cephei.dev.authentication_service.entity.User;
import cephei.dev.authentication_service.mapper.UserMapper;
import cephei.dev.authentication_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new UserDetailsImpl(user);
    }

    public UserReadDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return userMapper.toReadDto(user);
    }
}
