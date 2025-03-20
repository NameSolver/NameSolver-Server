package com.dongdong.nameSolver.domain.auth.domain.repository;

import com.dongdong.nameSolver.domain.auth.domain.entity.AuthToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class AuthRepository {
    private final EntityManager em;
    
    public AuthRepository(EntityManager em) {
        this.em = em;
    }

    public void save(UUID randomKey, String name) {
        em.persist(AuthToken.builder().key(randomKey).name(name).build());
    }

    public UUID findKeyByName(String name) {
        TypedQuery<UUID> query = em.createQuery("select AuthToken.key from AuthToken token where token.name = :name", UUID.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }
}
