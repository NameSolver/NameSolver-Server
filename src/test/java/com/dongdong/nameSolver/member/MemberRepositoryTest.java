package com.dongdong.nameSolver.member;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional
    void 멤버가져오기(){
        Member member = Member.builder().id("dong").build();
        memberRepository.save(member);
        boolean result = memberRepository.existsById("lee");

        Assertions.assertThat(result).isFalse();
    }
}
