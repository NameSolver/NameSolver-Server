package com.dongdong.nameSolver.domain.member.application.service;

import com.dongdong.nameSolver.domain.member.application.dto.response.MemberResponse;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 멤버 정보 가져오는 메서드
     */
    public MemberResponse findMember(UUID memberId) {
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));
        return MemberResponse.from(member);
    }

    /**
     * 로그아웃 메서드
     */
    @Transactional
    public void signout(UUID memberId) {
        // 멤버 가져오기
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("존재하지 않는 유저입니다."));

        // 리프레시 토큰 삭제
        member.removeRefreshToken();
    }
}
