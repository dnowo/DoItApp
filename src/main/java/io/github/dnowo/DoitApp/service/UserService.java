package io.github.dnowo.DoitApp.service;

import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUser(UsernamePasswordAuthenticationToken userAuthenticated) {
        return userRepository.findByUsername(userAuthenticated.getPrincipal().toString());
    }
}
