package com.dongdong.nameSolver.domain.match.application.service;

import com.dongdong.nameSolver.domain.match.domain.repository.MatchRecordRepository;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchExpireListener {
    private final MatchProcessor matchProcessor;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void onMatchExpire(Long matchId) {
        log.info("[rabbitMQ] {} 대결 종료", matchId);
        matchProcessor.processMatch(matchId);
    }
}
