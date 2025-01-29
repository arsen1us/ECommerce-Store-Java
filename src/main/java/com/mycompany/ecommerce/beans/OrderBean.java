package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Order;
import com.mycompany.ecommerce.security.JwtUtil;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;


@Named
@RequestScoped
public class OrderBean {
    
    private List<Order> orders;
    
    @PostConstruct
    public void init() {
        // Загрузить категории
        getByUserId();
    }
    
    // Загрузка заказов по id пользователя
    public void getByUserId(){
        try {
            // Получение токена из сессии
            String token = getJwtToken();

            String pureToken = token.replace("Bearer ", "");
        
            String userId = JwtUtil.parseTokenForId(pureToken);
        
            if (userId == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to extract user ID from token.", null));
                return;
            }
            
            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/order/" + userId);
            
            // Отправка GET-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", token)
                .get();
            
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешный ответ
                this.orders = response.readEntity(new GenericType<List<Order>>() {});
            } else {
                // Обработка ошибки
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка загрузки заказов", errorMessage));
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
    
    public List<Order> getOrders(){
        return this.orders;
    }
    public void setOrders(List<Order> orders){
        this.orders = orders;
    }
}
