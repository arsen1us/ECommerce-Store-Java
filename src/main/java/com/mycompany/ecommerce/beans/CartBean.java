package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Cart;
import com.mycompany.ecommerce.security.JwtUtil;
import com.mycompany.ecommerce.Order;
import com.mycompany.ecommerce.OrderItem;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;

import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class CartBean {
    
    private List<Cart> carts;
    
    private double totalCartPrice;
    
    @PostConstruct
    public void init() {
        // Загрузить корзину
        loadCart();
        // Посчитать стоимость кроссовок в корзине
        calculateTotal();
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

    // Создать заказ
    public void createOrder()
    {
        try{
            // Создание клиента
            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/order");
        
            // Получение токена из сессии
            String token = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
            
            // Парсинг id пользователя из токена
            String pureToken = token.replace("Bearer ", "");
            String userId = JwtUtil.parseTokenForId(pureToken);
        
            // Получить выбранные элементы корзины
            List<Cart> selectedCarts = carts.stream()
                .filter(Cart::isSelected)
                .collect(Collectors.toList());
        
            // Создание заказа
            Order order = new Order();
            order.setUserId(Integer.parseInt(userId));
            order.setStatus("Создано");
        
            // Список элементов заказа
            List<OrderItem> orderItems = new ArrayList<>();
        
            // Создать элементы заказа
            for(Cart cart : selectedCarts)
            {
                OrderItem orderItem = new OrderItem();
                orderItem.setSneakerId(cart.getSneakerId());
                orderItem.setQuantity(1);
                orderItem.setPrice(cart.getPrice());
                
                orderItem.setOrder(order);
                
                orderItems.add(orderItem);
            }
        
            // Записать элементы заказа в заказ
            order.setOrderItems(orderItems);
        
            // Отправка POST-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", token)
                .post(Entity.entity(order, MediaType.APPLICATION_JSON));

            // Успешный ответ
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            String successMessage = response.readEntity(String.class); // "Заказ успешно создан"
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, successMessage, ""));
            
            // Удалить все заказанные элементы 
            carts.removeAll(selectedCarts);
            } 
            // Обработка ошибок
            else{
            String errorMessage = response.readEntity(String.class);
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка создания заказа", errorMessage));
            }
        }
        catch (Exception e) {
            // Логирование и уведомление об ошибке
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка соединения с API", e.getMessage()));
        }
    }
    
    // Посчитать итоговую стоимость корзины
    public void calculateTotal()
    {
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
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/price/" + userId);
            
            // Отправка GET-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", token)
                .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешный ответ
                this.totalCartPrice = response.readEntity(Double.class);
            } else {
                // Обработка ошибки
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка загрузки стоимости корзины", errorMessage));
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
    
    
    
    
    public List<Cart> getCarts() {
        return this.carts;
    }
    public void setCarts(List<Cart> carts) {
        this.carts = carts;
    }
    
    public double getTotalCartPrice(){
        return this.totalCartPrice;
    }
    public void setTotalCartPrice(double totalCartPrice){
        this.totalCartPrice = totalCartPrice;
    }
}
