package com.dongdong.nameSolver.domain.member.application.service;

import com.dongdong.nameSolver.domain.member.application.dto.response.MemberResponse;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberResponse findMember(UUID memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return MemberResponse.from(member);
    }
}
