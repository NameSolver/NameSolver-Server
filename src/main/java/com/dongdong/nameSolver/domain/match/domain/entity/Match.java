package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    private LocalDateTime startedAt;
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    private MatchType matchType;

    private int requesterStartRating;
    private int accepterStartRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member accepter;
}
