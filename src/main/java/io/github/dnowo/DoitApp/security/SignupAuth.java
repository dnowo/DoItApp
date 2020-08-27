package io.github.dnowo.DoitApp.security;

import lombok.Getter;

import javax.validation.constraints.Email;
import java.util.Set;

@Getter
public class SignupAuth {
    private String username;

    private String password;

    @Email
    private String email;

    private Set<String> role;


}
