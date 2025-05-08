package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchCandidate;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MatchRepository {
    private EntityManager em;

    public MatchRepository(EntityManager em) {
        this.em = em;
    }

    public List<MatchCandidate> findByMatchType(MatchType matchType) {
        return em.createQuery("select matchrequest from MatchCandidate matchrequest inner join matchrequest.match match where match.matchType = :matchType", MatchCandidate.class)
                .setParameter("matchType", matchType)
                .getResultList();
    }

    public Match save(Match match) {
        em.persist(match);
        return match;
    }

    public MatchCandidate request(Match match, Member member) {
        MatchCandidate matchCandidate = new MatchCandidate(member, match);
        em.persist(matchCandidate);
        return matchCandidate;
    }
}
