package com.dongdong.nameSolver.domain.auth.presentation;

import com.dongdong.nameSolver.domain.auth.application.dto.request.CreateAuthKeyCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.ReissueCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignInCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthKeyResponse;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthTokenResponse;
import com.dongdong.nameSolver.domain.auth.application.dto.response.SignUpResponse;
import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import com.dongdong.nameSolver.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/key")
    public ApiResponse<AuthKeyResponse> createKey(@RequestBody CreateAuthKeyCommand createAuthKeyCommand) {
        return ApiResponse.success(authService.createKey(createAuthKeyCommand));
    }

    @PostMapping("/login")
    public ApiResponse<AuthTokenResponse> signIn(@RequestBody SignInCommand signInCommand) {
        return ApiResponse.success(authService.signIn(signInCommand));
    }

    @PostMapping("/join")
    public ApiResponse<SignUpResponse> signUp(@RequestBody SignUpCommand signUpCommand) {
        return ApiResponse.success(authService.signUp(signUpCommand));
    }

    @PostMapping("/reissue")
    public ApiResponse<AuthTokenResponse> reissue(@RequestBody ReissueCommand reissueCommand) {
        return ApiResponse.success(authService.reissue(reissueCommand));
    }
}
