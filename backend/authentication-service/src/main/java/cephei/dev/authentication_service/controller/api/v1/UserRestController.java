package cephei.dev.authentication_service.controller.api.v1;

import cephei.dev.authentication_service.dto.AuthMe;
import cephei.dev.authentication_service.dto.UserClientResponse;
import cephei.dev.authentication_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{username}")
    public UserClientResponse findByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @GetMapping("/me")
    public AuthMe authMe(
            @AuthenticationPrincipal String username
    ) {
        System.out.println(username);
        return userService.authMe(username);
    }
}
