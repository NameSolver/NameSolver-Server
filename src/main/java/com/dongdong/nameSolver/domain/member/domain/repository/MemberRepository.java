package com.dongdong.nameSolver.domain.member.domain.repository;

import com.dongdong.nameSolver.domain.member.domain.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
public class MemberRepository {
    private EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public boolean existsById(String id) {
        List<Member> members = em.createQuery("select member from Member member where member.id = :id", Member.class)
                .setParameter("id", id)
                .getResultList();

        return !members.isEmpty();
    }

    public boolean existsBySolvedacName(String solvedacName) {
        List<Member> members = em.createQuery("select member from Member member where member.solvedacName = :solvedacName", Member.class)
                .setParameter("solvedacName", solvedacName)
                .getResultList();
        return !members.isEmpty();
    }


    public Optional<Member> findById(String id) {
        Stream<Member> member = em.createQuery("select member from Member member where member.id = :id", Member.class)
                .setParameter("id", id)
                .getResultStream();
        return member.findAny();
    }

    public Optional<Member> findByMemberId(UUID memberId) {
        Stream<Member> member = em.createQuery("select member from Member member where member.memberId = :memberId", Member.class)
                .setParameter("memberId", memberId)
                .getResultStream();
        return member.findAny();
    }
}
