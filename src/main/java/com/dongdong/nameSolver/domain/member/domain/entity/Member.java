package com.dongdong.nameSolver.domain.member.domain.entity;

import com.dongdong.nameSolver.domain.auth.application.dto.SignUpDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID memberId;

    private String name;
    private String solvedacName;
    private String id;
    private String email;
    private String password;
    private String refreshToken;

    @Builder
    public Member(String name, String solvedacName, String id, String email, String password) {
        this.name = name;
        this.solvedacName = solvedacName;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static Member join(SignUpDto signUpDto, String hashedPassword) {
        return Member.builder()
                .id(signUpDto.getId())
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .solvedacName(signUpDto.getSolvedacName())
                .password(hashedPassword)
                .build();
    }

    public void storeRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
