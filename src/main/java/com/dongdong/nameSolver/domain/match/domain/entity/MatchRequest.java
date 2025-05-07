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
public class MatchRequest {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchRequestId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(value = EnumType.STRING)
    private MatchType matchType;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member requester;

    private MatchRequest(MatchType matchType, Member requester) {
        this.matchType = matchType;
        this.requester = requester;
    }

    public static MatchRequest create(MatchType matchType, Member member) {
        return new MatchRequest(matchType, member);
    }
}
