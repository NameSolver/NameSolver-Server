package com.dongdong.nameSolver.auth;

import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private AuthService loginService;

    @Test
    void 인증키발급(){
        //인증키 발급
        loginService.generateKey("lmkn5342");

        //인증키 확인 요청 -> 인증 확인 -> 회원 정보 생성
        loginService.signIn();
    }

    @Transactional
    @Test
    void 크롤링() throws IOException {
        String key = loginService.extractKey("lmkn5342");
        Assertions.assertThat(key).isEqualTo("김동동");
    }
}
