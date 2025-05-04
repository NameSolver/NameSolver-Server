package com.dongdong.nameSolver.member;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignInCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.auth.application.dto.response.AuthTokenResponse;
import com.dongdong.nameSolver.domain.auth.application.service.AuthService;
import com.dongdong.nameSolver.domain.member.application.dto.response.MemberResponse;
import com.dongdong.nameSolver.domain.member.application.service.MemberService;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import com.dongdong.nameSolver.global.jwt.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private AuthService loginService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;

    private UUID memberId;

    @BeforeEach
    @Transactional
    void 유저_세팅() {
        //회원 생성
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        Member save = memberRepository.save(Member.join(signUpCommand, hashedPassword));

        memberId = save.getMemberId();
    }

    @Test
    @Transactional
    void 유저_가져오기() {
        MemberResponse member = memberService.findMember(memberId);
        Assertions.assertThat(member.getName()).isEqualTo("김동현");
    }
}
