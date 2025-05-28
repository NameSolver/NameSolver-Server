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

    private MatchRecord(LocalDateTime startedAt, LocalDateTime endAt, MatchType matchType, int winnerIncreaseRating, int loserIncreaseRating, Member winner, Member loser) {
        this.startedAt = startedAt;
        this.endAt = endAt;
        this.matchType = matchType;
        this.winnerIncreaseRating = winnerIncreaseRating;
        this.loserIncreaseRating = loserIncreaseRating;
        this.winner = winner;
        this.loser = loser;
    }

    public static MatchRecord quitMatch(Match match, int requesterEndRating, int accepterEndRating) {

        int accepterIncreaseRating = accepterEndRating - match.getAccepterStartRating();
        int requesterIncreaseRating = requesterEndRating - match.getRequesterStartRating();
        Member winner = null;
        Member loser = null;

        if(accepterIncreaseRating > requesterIncreaseRating) {
            winner = match.getAccepter();
            loser = match.getRequester();
        }
        else {
            winner = match.getRequester();
            loser = match.getAccepter();
        }

        return new MatchRecord(match.getStartAt(), match.getEndAt(), match.getMatchType(), Math.max(accepterIncreaseRating, requesterIncreaseRating), Math.min(accepterIncreaseRating, requesterIncreaseRating), winner, loser);
    }
}
