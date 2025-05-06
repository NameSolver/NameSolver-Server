package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchRequestResponse;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;

@Entity
public class MatchRequestCandidate {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "match_request_id")
    private MatchRequest match;

    @Enumerated(value = EnumType.STRING)
    private MatchRequestResponse response;
}
