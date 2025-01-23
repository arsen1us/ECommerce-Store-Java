package com.mycompany.ecommerce;

import com.mycompany.ecommerce.Sneaker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.List;

@Transactional
public class SneakerRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    // Получить список всех кроссовок
    public List<Sneaker> getAllSneakers() {
        return entityManager
            .createQuery("SELECT s FROM Sneaker s", Sneaker.class)
            .getResultList();
    }
    
    // Получить кроссовки по id
    public Sneaker getSneakerById(long id) {
        Sneaker existingSneaker = entityManager.find(Sneaker.class, id);
        return existingSneaker;
}
    
    // Добавить кроссовки
    @Transactional
    public Sneaker addSneaker(Sneaker sneaker) {
        try{
            entityManager.persist(sneaker);
            return sneaker;
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    // Изменить кроссовки
    public Sneaker updateSneaker(Sneaker sneaker) {
        return entityManager.merge(sneaker);
    }
    
    // Удалить кроссовки по id
    public void deleteSneakerById(long id) {
        Sneaker sneaker = entityManager.find(Sneaker.class, id);
        if (sneaker != null) {
            entityManager.remove(sneaker);
        }
    }
}
