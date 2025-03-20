package com.dongdong.nameSolver.domain.member.domain.repository;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import com.dongdong.nameSolver.domain.test.domain.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {
    private EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }
    public Member save(String name, String password) {
        Member member = Member.builder().name(name).password(password).build();
        em.persist(member);
        return member;
    }
}
