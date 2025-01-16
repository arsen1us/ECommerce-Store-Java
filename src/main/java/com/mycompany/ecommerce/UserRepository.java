package com.mycompany.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;

@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Optional<User> findByUsername(String name) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", name)
                .getResultStream()
                .findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    public void save(User user) {
        entityManager.persist(user);
    }
}
