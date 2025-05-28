package com.dongdong.nameSolver.domain.match.domain.repository;

import com.dongdong.nameSolver.domain.match.domain.entity.MatchRecord;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class MatchRecordRepository {
    private final EntityManager em;

    public MatchRecordRepository(EntityManager em) {
        this.em = em;
    }

    public void save(MatchRecord matchRecord) {
        em.persist(matchRecord);
    }
}
