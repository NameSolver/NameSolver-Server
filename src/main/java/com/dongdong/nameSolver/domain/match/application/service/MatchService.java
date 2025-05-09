package com.dongdong.nameSolver.domain.match.application.service;

import com.dongdong.nameSolver.domain.match.application.dto.request.CreateMatchCommand;
import com.dongdong.nameSolver.domain.match.application.dto.response.MatchResponse;
import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchService {

    private final MemberRepository memberRepository;
    private final MatchRepository matchRepository;

    /**
     * 대결 생성 메서드
     */
    public MatchResponse createMatch(UUID memberId, CreateMatchCommand createMatchCommand) {
        // 멤버 확인
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // 매치 생성
        Match match = matchRepository.save(Match.create(createMatchCommand.getMatchType(), member));

        // 대결 타입에 맞는 유저 찾기
        List<Member> members = getMemberByType(createMatchCommand.getMatchType(), member);

        // 유저들에게 대결 요청 보내기
        members.forEach(findMember -> matchRepository.request(match, findMember));

        return new MatchResponse(match.getMatchId());
    }

    private List<Member> getMemberByType(MatchType type, Member member) {
        List<Member> members = new ArrayList<>();

        // 대결 타입에 따라 분기
        // 1. 동명이인 : 동명이인 찾아서 매치 연결
        // 2. 동성 : 같은 성 찾아서 매치 연결
        // 3. 초성 : 초성 같은 사람 찾아서 매치 연결 -> 보류
        switch (type) {
            case SAME_FULL_NAME -> members = memberRepository.findSameName(member);
            case SAME_LAST_NAME -> members = memberRepository.findSameLastName(member);
            case SAME_FIRST_CONSONANT -> members = null;
        }

        return members;
    }

    /**
     * 대결 수락 메서드
     */
    @Transactional
    public void acceptMatch(UUID memberId, Long matchId) {
        // 멤버 확인
        Member member = memberRepository.findByMemberId(memberId).orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // 대결 확인
        Match match = matchRepository.findMatchById(matchId).orElseThrow(() -> new RuntimeException("해당하는 대결이 존재하지 않습니다."));

        // 대결이 이미 승인된 상태인지 확인
        if(match.getAccepter() != null) {
            throw new RuntimeException("이미 승인된 대결입니다.");
        }

        // 멤버들의 solvedAC rating 가져오기
        int requesterStartRating = 0;
        int accepterStartRating = 0;

        // 업데이트
        match.acceptMatch(requesterStartRating, accepterStartRating, member);

    }
}

