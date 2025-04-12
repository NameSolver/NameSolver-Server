package com.dongdong.nameSolver.domain.member.domain.repository;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Member member) {
        em.persist(member);
    }

    public boolean existsById(String id) {
        return em.createQuery("select member from Member member where member.id = :id", Member.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .isPresent();
    }

    public boolean existsBySolvedacName(String solvedacName) {
        return em.createQuery("select member from Member member where member.solvedacName = :solvedacName", Member.class)
                .setParameter("solvedacName", solvedacName)
                .getResultStream()
                .findFirst()
                .isPresent();
    }
}
