package com.dongdong.nameSolver.domain.match.application.service;

import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchAsyncService {

    private final MatchRepository matchRepository;
    private final MatchProcessor matchProcessor;

    @Async
    @Transactional
    public void handleMatchesAsync() {
        List<Match> lastMatch = matchRepository.findByEndDate();

        long start = System.currentTimeMillis();
        lastMatch.parallelStream().forEach(matchProcessor::processMatch);
        long end = System.currentTimeMillis();

        log.info("[match done] {}ms", end - start);
    }
}
