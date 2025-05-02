package com.dongdong.nameSolver.domain.auth.presentation;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignInCommand;
import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/key")
    public void createKey() {
        authService.generateKey();
    }

    @PostMapping("/login")
    public void signIn(SignInCommand signInDto) {
        authService.signIn();
    }

    @PostMapping("/join")
    public void signUp() {

    }

    @PostMapping("/logout")
    public void signOut() {

    }

    @PostMapping("/reissue")
    public void reissue() {

    }
}
