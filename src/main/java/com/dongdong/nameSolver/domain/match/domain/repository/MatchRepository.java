package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchRequestCandidate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchRepository {
    public List<MatchRequestCandidate> findByMatchType() {
        return null;
    }
}
