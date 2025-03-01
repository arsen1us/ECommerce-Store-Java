package com.mycompany.ecommerce;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
public class Cart {

    // id корзины
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // id пользователя
    @Column(nullable = false)
    private Long userId;

    // id пары обуви
    @Column(nullable = false)
    private Long sneakerId;
    
    @Transient
    // Цена кроссовок в корзине (не сохранять в бд)
    private double price;
    
    @Transient
    // Выбран? (не сохранять в бд)
    private boolean selected;
    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSneakerId() {
        return sneakerId;
    }
    public void setSneakerId(Long sneakerId) {
        this.sneakerId = sneakerId;
    }
    
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
