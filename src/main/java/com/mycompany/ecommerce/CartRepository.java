package com.mycompany.ecommerce;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

@Transactional
@ApplicationScoped
public class CartRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Получить список всех записей в корзине
    public List<Cart> getAllCarts() {
        return entityManager
            .createQuery("SELECT c FROM Cart c", Cart.class)
            .getResultList();
    }

    // Получить запись из корзины по id
    public Cart getCartById(long id) {
        return entityManager.find(Cart.class, id);
    }

    // Получить список записей корзины по userId
    public List<Cart> getCartsByUserId(long userId) {
        return entityManager
            .createQuery("SELECT c FROM Cart c WHERE c.userId = :userId", Cart.class)
            .setParameter("userId", userId)
            .getResultList();
    }

    // Добавить новую запись в корзину
    public Cart addCart(Cart cart) {
        try {
            entityManager.persist(cart);
            return cart;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // Удалить запись из корзины по id
    public void deleteCartById(long id) {
        Cart cart = entityManager.find(Cart.class, id);
        if (cart != null) {
            entityManager.remove(cart);
        }
    }
}