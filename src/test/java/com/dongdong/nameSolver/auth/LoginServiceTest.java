package com.dongdong.nameSolver.auth;

import com.dongdong.nameSolver.domain.auth.application.dto.KeyDto;
import com.dongdong.nameSolver.domain.auth.application.dto.SignInDto;
import com.dongdong.nameSolver.domain.auth.application.dto.SignUpDto;
import com.dongdong.nameSolver.domain.auth.application.dto.TokenDto;
import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;

@Slf4j
@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private AuthService loginService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 인증키_발급(){
        //인증키 발급
        KeyDto key = loginService.generateKey("lmkn5342");
        System.out.println("key: " + key.getKey());
    }

    @Test
    @Transactional
    void 회원가입() {
        SignUpDto signUpDto = new SignUpDto("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        loginService.signUp(signUpDto);

        boolean result = memberRepository.existsBySolvedacName("lmkn5342");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @Transactional
    void 로그인_아이디_불일치() {
        // 아이디, 비밀번호 가져오기
        SignInDto signInDto = new SignInDto();
        signInDto.setId("lmkn534");
        signInDto.setPassword("as");

        TokenDto token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @Transactional
    void 로그인_아이디_일치_비번_불일치() {
        // 아이디, 비밀번호 가져오기
        SignInDto signInDto = new SignInDto();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("as");

        TokenDto token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNull();
    }

    @Test
    @Transactional
    void 로그인_성공() {
        // 아이디, 비밀번호 가져오기
        SignInDto signInDto = new SignInDto();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("asdf");

        TokenDto token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNotNull();
    }
}
