package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.model.ERole;
import io.github.dnowo.DoitApp.model.Role;
import io.github.dnowo.DoitApp.model.User;
import io.github.dnowo.DoitApp.repository.RoleRepository;
import io.github.dnowo.DoitApp.repository.UserRepository;
import io.github.dnowo.DoitApp.security.LoginAuth;
import io.github.dnowo.DoitApp.security.MessageResponse;
import io.github.dnowo.DoitApp.security.SignupAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class LoginController {

    UserRepository userRepository;
    RoleRepository roleRepository;
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public void login(@Valid @RequestBody LoginAuth loginAuth) {
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupAuth signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Username is already taken."));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Email is already taken."));
        }

        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail());

        Set<String> stringRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<Role>();

        if (stringRoles == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role is not found!"));
            roles.add(userRole);
        } else {
            stringRoles.forEach(role -> {
                        switch (role) {
                            case "ROLE_ADMIN":
                                Role adminR = roleRepository
                                        .findByName(ERole.ROLE_ADMIN)
                                        .orElseThrow(() -> new RuntimeException("Role is not found!"));
                                roles.add(adminR);
                                break;
                            case "ROLE_USER":
                                Role userR = roleRepository
                                        .findByName(ERole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Role is not found!"));
                                roles.add(userR);
                        }
                    }
            );
        }

        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
