package com.mycompany.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@Transactional
@ApplicationScoped
public class CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить список всех категорий
    public List<Category> getAll() {
        return entityManager
            .createQuery("SELECT c FROM Category c", Category.class)
            .getResultList();
    }
}
