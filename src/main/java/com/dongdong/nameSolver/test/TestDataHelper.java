package com.dongdong.nameSolver.test;

import com.dongdong.nameSolver.domain.auth.application.dto.request.SignUpCommand;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TestDataHelper {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public List<UUID> setUser() {
        List<UUID> memberIds = new ArrayList<>();
        memberIds.add(createUser("김동현"));
        memberIds.add(createUser("김동현"));
        memberIds.add(createUser("김동현"));
        memberIds.add(createUser("김도현"));
        memberIds.add(createUser("기도현"));
        return memberIds;
    }

    private UUID createUser(String name) {
        SignUpCommand signUpCommand = new SignUpCommand(name, UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString(), UUID.randomUUID().toString());
        String hashedPassword = passwordEncoder.encode(signUpCommand.getPassword());
        return memberRepository.save(Member.join(signUpCommand, hashedPassword)).getMemberId();
    }
}
