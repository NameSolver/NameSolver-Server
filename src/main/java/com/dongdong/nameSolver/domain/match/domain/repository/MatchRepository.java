package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.constant.MatchType;
import com.dongdong.nameSolver.domain.match.domain.entity.Match;
import com.dongdong.nameSolver.domain.match.domain.entity.MatchCandidate;
import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.hibernate.LockMode;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    public Optional<Match> findMatchById(Long id) {
        Stream<Match> result = em.createQuery("select match from Match match where match.id = :id", Match.class)
                .setParameter("id", id)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .getResultStream();
        return result.findAny();
    }

    public List<Match> findByEndDate() {
        return em.createQuery("select match from Match match where match.endAt < :day", Match.class)
                .setParameter("day", LocalDateTime.now())
                .getResultList();
    }

    public void deleteMatchById(Long matchId) {
        em.createQuery("delete from Match match where match.matchId = :matchId")
                .setParameter("matchId", matchId)
                .executeUpdate();
    }
}
