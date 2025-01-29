package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Cart;
import com.mycompany.ecommerce.Sneaker;
import com.mycompany.ecommerce.security.JwtUtil;
import com.mycompany.ecommerce.Category;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class CategoryBean {
    
    private List<Category> categories;
    
    @PostConstruct
    public void init() {
        // Загрузить категории
        loadCategories();
    }
    
    public void loadCategories(){
        try {
            // Получение токена из сессии
            String token = getJwtToken();
            
            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/category");
            
            // Отправка GET-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", token)
                .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешный ответ
                this.categories = response.readEntity(new GenericType<List<Category>>() {});
            } else {
                // Обработка ошибки
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка загрузки категорий", errorMessage));
            }
        } catch (Exception e) {
            // Логирование и уведомление об ошибке
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка соединения с API", e.getMessage()));
        } 
    }
    
    // Получить jwt-токен
    private String getJwtToken() {
        return (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
    }
    
    public List<Category> getCategories() {
        return this.categories;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
