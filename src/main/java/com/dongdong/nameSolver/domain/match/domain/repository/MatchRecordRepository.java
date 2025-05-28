package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.entity.MatchRecord;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchRecordRepository {
    private final EntityManager em;

    public MatchRecordRepository(EntityManager em) {
        this.em = em;
    }

    public void save(MatchRecord matchRecord) {
        em.persist(matchRecord);
    }

    public List<MatchRecord> findAll() {
        return em.createQuery("select match from MatchRecord match", MatchRecord.class).getResultList();
    }
}
