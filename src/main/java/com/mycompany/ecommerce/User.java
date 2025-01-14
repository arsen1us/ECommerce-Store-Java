package com.mycompany.ecommerce;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    @Size(min = 5, max = 50)
    private String username;

    @NotBlank
    @Column(nullable = false)
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String role = "USER"; // По умолчанию - пользователь (USER)

    // Геттеры и сеттеры
}
