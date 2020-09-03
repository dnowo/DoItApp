package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/user")
    public User getUser(@AuthenticationPrincipal UsernamePasswordAuthenticationToken userAuthenticated){
        return userService.getUser(userAuthenticated);
    }
}
