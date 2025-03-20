package com.dongdong.nameSolver.domain.auth.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class AuthToken {
    @Id
    private UUID key;

    private String name;

    @Builder
    public AuthToken(UUID key, String name) {
        this.key = key;
        this.name = name;
    }
}
