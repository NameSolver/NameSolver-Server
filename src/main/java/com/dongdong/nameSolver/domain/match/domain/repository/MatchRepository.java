package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchRequest;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchRequestCandidate;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MatchRepository {
    private EntityManager em;

    public MatchRepository(EntityManager em) {
        this.em = em;
    }

    public List<MatchRequestCandidate> findByMatchType(MatchType matchType) {
        return em.createQuery("select matchrequest from MatchRequestCandidate matchrequest inner join matchrequest.match match where match.matchType = :matchType", MatchRequestCandidate.class)
                .setParameter("matchType", matchType)
                .getResultList();
    }

    public MatchRequest save(MatchRequest matchRequest) {
        em.persist(matchRequest);
        return matchRequest;
    }

    public MatchRequestCandidate request(MatchRequest matchRequest, Member member) {
        MatchRequestCandidate matchRequestCandidate = new MatchRequestCandidate(member, matchRequest);
        em.persist(matchRequestCandidate);
        return matchRequestCandidate;
    }
}
