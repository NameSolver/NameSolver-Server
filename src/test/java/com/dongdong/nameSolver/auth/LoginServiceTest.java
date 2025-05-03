package com.dongdong.nameSolver.auth;

import com.dongdong.nameSolver.domain.auth.application.dto.request.CreateAuthKeyCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.ReissueCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignInCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthTokenResponse;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthKeyResponse;
import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import com.dongdong.nameSolver.global.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@SpringBootTest
public class LoginServiceTest {
    @Autowired
    private AuthService loginService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider provider;
    @Autowired
    private AuthService authService;

    @Test
    void 인증키_발급(){
        //인증키 발급
        CreateAuthKeyCommand command = new CreateAuthKeyCommand();
        command.setSolvedacName("lmkn5342");
        AuthKeyResponse key = loginService.createKey(command);
        System.out.println("key: " + key.getKey());
    }

    @Test
    @Transactional
    void 회원가입() {
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        loginService.signUp(signUpCommand);

        boolean result = memberRepository.existsBySolvedacName("lmkn5342");
        Assertions.assertThat(result).isTrue();
    }

    @Test
    @Transactional
    void 로그인_아이디_불일치() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        memberRepository.save(Member.join(signUpCommand, hashedPassword));

        // 아이디, 비밀번호 가져오기
        SignInCommand signInDto = new SignInCommand();
        signInDto.setId("lmkn534");
        signInDto.setPassword("as");

        Assertions.assertThatThrownBy(() -> loginService.signIn(signInDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("해당하는 아이디가 없습니다.");
    }

    @Test
    @Transactional
    void 로그인_아이디_일치_비번_불일치() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        memberRepository.save(Member.join(signUpCommand, hashedPassword));

        // 아이디, 비밀번호 가져오기
        SignInCommand signInDto = new SignInCommand();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("as");

        Assertions.assertThatThrownBy(() -> loginService.signIn(signInDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("비밀번호가 불일치합니다.");
    }

    @Test
    @Transactional
    void 로그인_성공() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        Member save = memberRepository.save(Member.join(signUpCommand, hashedPassword));

        // 아이디, 비밀번호 가져오기
        SignInCommand signInDto = new SignInCommand();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("asdf");

        // 토큰 반환 확인
        AuthTokenResponse token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNotNull();

        // 토큰 정보 확인
        String memberId = provider.extractSubject(token.getAccessToken());
        Assertions.assertThat(memberId).isEqualTo(save.getMemberId().toString());
    }

    @Test
    @Transactional
    void 토큰_재발급_유저_미존재() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        Member save = memberRepository.save(Member.join(signUpCommand, hashedPassword));

        // 아이디, 비밀번호 가져오기
        SignInCommand signInDto = new SignInCommand();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("asdf");

        // 토큰 반환 확인
        AuthTokenResponse token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNotNull();

        // 토큰 정보 확인
        String memberId = provider.extractSubject(token.getAccessToken());
        Assertions.assertThat(memberId).isEqualTo(save.getMemberId().toString());

        // 토큰 재발급
        Assertions.assertThatThrownBy(() -> {
            authService.reissue(new ReissueCommand("", UUID.randomUUID()));
        }).hasMessage("해당하는 아이디가 없습니다.");
    }

    @Test
    @Transactional
    void 토큰_재발급_토큰_미일치() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        Member save = memberRepository.save(Member.join(signUpCommand, hashedPassword));

        // 아이디, 비밀번호 가져오기
        SignInCommand signInDto = new SignInCommand();
        signInDto.setId("lmkn5342");
        signInDto.setPassword("asdf");

        // 토큰 반환 확인
        AuthTokenResponse token = loginService.signIn(signInDto);
        Assertions.assertThat(token.getAccessToken()).isNotNull();

        // 토큰 정보 확인
        String memberId = provider.extractSubject(token.getAccessToken());
        Assertions.assertThat(memberId).isEqualTo(save.getMemberId().toString());

        // 토큰 재발급
        AuthTokenResponse reissue = authService.reissue(new ReissueCommand(save.getRefreshToken(), save.getMemberId()));
        Assertions.assertThat(reissue.getAccessToken()).isNotNull();
    }
}
