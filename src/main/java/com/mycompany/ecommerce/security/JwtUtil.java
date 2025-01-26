package com.mycompany.ecommerce.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    public static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 час

    // Генерация токена
    public static String generateToken(String email, long userId, String role) {
        return Jwts.builder()
                .setSubject(email) // email пользователя
                .claim("id", userId)  // id пользователя
                .claim("role", role) // role пользователя
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // Валидация токена
    public static String validateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
    
    // Извлечение id пользователя из токена
    public static String parseTokenForId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                                .setSigningKey(key)
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
            // Получаем id пользователя из claims
            
             String id = claims.get("id").toString();
             if(id == null){
                 return "";
             }
             else{
                 return id;
             }
        } catch (Exception ex) {
            System.out.print("Не удалось спарсить id пользователя. Детали: " + ex.getMessage());
            return "";
        }
    }
}
