package com.dongdong.nameSolver.domain.member.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String password;

    @Builder
    public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }

}
