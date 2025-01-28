package com.mycompany.ecommerce;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
@Entity
@Table(name = "sneakers")
public class Sneaker {

    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Название модели
    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;
    
    // Бренд
    @NotBlank
    @Column(nullable = false)
    private String brand;
    
    // Цена
    @NotNull
    @Column(nullable = false)
    private double price;
    
    // Размер
    @NotBlank
    @Column(nullable = false)
    private String size;
    
    // Цвет
    @NotBlank
    @Column(nullable = false)
    private String color;
    
    // Кол-во на складе
    @NotNull
    @Column(nullable = false)
    private int count;
    
    // Наличие на складе
    @NotNull
    @Column(nullable = false)
    private boolean available;
    
    // Id категории
    @Column(nullable = true)
    private Long categoryId;
    
    // Путь до изображения кроссовок 
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    
    public void setId(Long id)
    {
        this.id = id;
    }
    public Long getId()
    {
        return id;
    }
    
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    public String getBrand(){
        return this.brand;
    }
    public void setBrand(String brand){
        this.brand = brand;
    }
    
    public double getPrice(){
        return this.price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    
    public String getSize(){
        return this.size;
    }
    public void setSize(String size){
        this.size = size;
    }
    
    public String getColor(){
        return this.color;
    }
    public void setColor(String color){
        this.color = color;
    }
    
    public int getCount(){
        return count;
    }
    public void setCount(int count){
        this.count = count;
    }
    
    public Boolean getAvailable(){
        return this.available;
    }
    public void setAvailable(Boolean available){
        this.available = available;
    }
    
    public Long getCategoryId()
    {
        return this.categoryId;
    }
    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }
    
    public String getImageUrl(){
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl){
        this.imageUrl = imageUrl;
    }
}
