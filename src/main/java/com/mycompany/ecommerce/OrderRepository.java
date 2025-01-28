package com.mycompany.ecommerce;

import com.mycompany.ecommerce.Sneaker;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@Transactional
@ApplicationScoped
public class OrderRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public void createOrder(Order order) {
        try{
            // Сохранение заказа вместе с элементами заказа
            entityManager.persist(order);
        }
        catch (Exception ex){
            System.out.print("Не удалось создать заказ с элементами заказа. Детали: " + ex.getMessage());
        }
    }
}
