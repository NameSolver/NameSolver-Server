package com.dongdong.nameSolver.domain.match.domain.entity;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
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
}
