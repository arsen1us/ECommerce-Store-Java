package com.mycompany.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;

@Transactional
@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить пользователя по почте
    public Optional<User> findByEmail(String email) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    // Получить пользователя по id
    public Optional<User> findById (long id) {
        return entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }
    
    // Добавить нового пользователя
    public void save(User user) {
        entityManager.persist(user);
    }
}
