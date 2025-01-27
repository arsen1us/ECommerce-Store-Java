package com.mycompany.ecommerce.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ecommerce.Sneaker;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.URI;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import com.mycompany.ecommerce.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.mycompany.ecommerce.Cart;
import com.mycompany.ecommerce.security.JwtUtil;
import jakarta.ws.rs.core.GenericType;

@Named
@RequestScoped
public class CartBean {
    
    private List<Cart> carts;
    
    @PostConstruct
    public void init() {
        loadCart();
    }
    
    // Загрузка корзины
    public void loadCart(){
        try {
            // Получение токена из сессии
            String token = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
            
            // Парсинг id пользователя из токена
            String pureToken = token.replace("Bearer ", "");
            String userId = JwtUtil.parseTokenForId(pureToken);
            
            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/" + userId);
            
            // Отправка GET-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", token)
                .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешный ответ
                this.carts = response.readEntity(new GenericType<List<Cart>>() {});
            } else {
                // Обработка ошибки
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка загрузки корзины", errorMessage));
            }
        } catch (Exception e) {
            // Логирование и уведомление об ошибке
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка соединения с API", e.getMessage()));
        } 
    }
    
    // Удаление кроссовок из корзины
    public String deleteFromCart(long cartId) {
//        try {
//            URL url = new URL("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/" + sneaker.getId());
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("DELETE");
//            connection.setRequestProperty("Authorization", "Bearer " + getJwtToken());
//            connection.setRequestProperty("Accept", "application/json");
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                System.out.println("Sneaker deleted successfully.");
//                this.sneaker = null; // Убираем объект после удаления
//            } else {
//                System.out.println("Failed to delete sneaker. Response code: " + responseCode);
//            }
//        } catch (Exception ex) {
//            System.out.println("Error deleting sneaker: " + ex.getMessage());
//        }
return "";
    }

    // Получить jwt-токен
    private String getJwtToken() {
        return (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
    }
    
    
    public List<Cart> getCarts() {
        return carts;
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
}
