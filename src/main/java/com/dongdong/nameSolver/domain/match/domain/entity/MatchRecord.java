package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MatchRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchRecordId;

    private LocalDateTime startedAt;
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    private MatchType matchType;

    private int winnerIncreaseRating;
    private int loserIncreaseRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    private Member winner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loser_id")
    private Member loser;
}
