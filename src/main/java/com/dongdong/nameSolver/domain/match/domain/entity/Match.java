package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "match_request")
public class Match {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchId;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @Enumerated(value = EnumType.STRING)
    private MatchType matchType;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    @ManyToOne
    @JoinColumn(name = "accepter_id")
    private Member accepter;

    private int requesterStartRating;
    private int accepterStartRating;

    private Match(MatchType matchType, Member requester) {
        this.matchType = matchType;
        this.requester = requester;
    }

    public static Match create(MatchType matchType, Member member) {
        return new Match(matchType, member);
    }

    public Match(LocalDateTime startAt, LocalDateTime endAt, MatchType matchType, Member requester, Member accepter, int requesterStartRating, int accepterStartRating) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.matchType = matchType;
        this.requester = requester;
        this.accepter = accepter;
        this.requesterStartRating = requesterStartRating;
        this.accepterStartRating = accepterStartRating;
    }

    public void acceptMatch(int requesterStartRating, int accepterStartRating, Member accepter) {
        this.requesterStartRating = requesterStartRating;
        this.accepterStartRating = accepterStartRating;
        this.accepter = accepter;

        this.startAt = LocalDateTime.now();
        this.endAt = startAt.plusDays(1);
    }
}
