package io.github.dnowo.DoitApp.controller;

import io.github.dnowo.DoitApp.security.LoginAuth;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody LoginAuth loginAuth){
    }
}
