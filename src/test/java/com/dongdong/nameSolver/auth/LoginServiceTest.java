package com.dongdong.nameSolver.auth;

import com.dongdong.nameSolver.domain.auth.application.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private LoginService loginService;

    @Test
    void 인증키발급(){
        //인증키 발급
        loginService.generateKey();

        //인증키 확인 요청 -> 인증 확인 -> 회원 정보 생성
        loginService.signIn();
    }
}
