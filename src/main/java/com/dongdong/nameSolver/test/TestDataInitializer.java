package com.dongdong.nameSolver.test;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TestDataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        SignUpCommand signUpCommand = new SignUpCommand("김동현", "lmkn", "lmkn5342", "lmkn5342", "asdf");
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        memberRepository.save(Member.join(signUpCommand, hashedPassword));
    }
}