package com.dongdong.nameSolver.domain.match.application.service;

import com.dongdong.nameSolver.domain.match.application.dto.request.CreateMatchCommand;
import com.dongdong.nameSolver.domain.match.application.dto.response.MatchResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MatchService {

    /**
     * 대결 생성 메서드
     */
    public MatchResponse createMatch(UUID memberId, CreateMatchCommand createMatchCommand) {
        // 멤버 확인
        
        // 매치 생성

        // 대결 타입에 따라 분기
        // 1. 동명이인 : 동명이인 찾아서 매치 연결
        // 2. 동성 : 같은 성 찾아서 매치 연결
        // 3. 초성 : 초성 같은 사람 찾아서 매치 연결

        return null;
    }
}
