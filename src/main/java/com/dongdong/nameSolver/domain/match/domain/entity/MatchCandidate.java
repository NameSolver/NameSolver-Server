package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class MatchCandidate {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    public MatchCandidate(Member member, Match match) {
        this.member = member;
        this.match = match;
    }
}
