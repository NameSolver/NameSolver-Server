package com.dongdong.nameSolver.domain.auth.application.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginService {

    public void generateKey() {
        // 랜덤 키 생성
        UUID randomKey = UUID.randomUUID();

        // 키 DB에 저장
        
        // 키 반환
    }

    public void signIn() {
    }
}
