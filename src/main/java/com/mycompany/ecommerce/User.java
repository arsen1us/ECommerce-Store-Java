package com.mycompany.ecommerce;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    // Id пользователя
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя пользователя
    @NotBlank
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 50)
    private String name;

    // Пароль пользователя 
    @NotBlank
    @Column(nullable = false)
    @Size(min = 8)
    private String password;
    
    // Почта пользователя
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;
    
    // Роль пользователя (По умолчанию - пользователь (USER))
    @Column(nullable = false)
    private String role = "USER";
    
    // Аватар пользователя
    @Column(name = "avatar_url", nullable = false)
    private String avatarUrl;
    
    public void setId(Long id)
    {
        this.id = id;
    }
    public Long getId()
    {
        return id;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return name;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    public String getPassword()
    {
        return password;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEmail()
    {
        return email;
    }
    
    public void setRole(String role)
    {
        this.role = role;
    }
    public String getRole()
    {
        return role;
    }
    
    public String getAvatarUrl(){
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl){
        this.avatarUrl = avatarUrl;
    }
}
