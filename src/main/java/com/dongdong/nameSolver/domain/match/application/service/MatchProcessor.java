package com.dongdong.nameSolver.domain.match.application.service;

import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchRecord;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRecordRepository;
import com.dongdong.nameSolver.domain.match.domain.repository.MatchRepository;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchProcessor {
    private final MatchRecordRepository matchRecordRepository;
    private final MatchRepository matchRepository;

    @Transactional
    public void processMatch(Match match) {
        int accepterEndRating = getSolvedAcRating(match.getAccepter());
        int requesterEndRating = getSolvedAcRating(match.getRequester());
        MatchRecord matchRecord = MatchRecord.quitMatch(match, requesterEndRating, accepterEndRating);
        matchRecordRepository.save(matchRecord);
        matchRepository.deleteMatchById(match.getMatchId());
    }

    private int getSolvedAcRating(Member member) {
        int rating = (int)(Math.random() * 10);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("{} rating : {}", member.getMemberId(), rating);
        return rating;
    }
}
