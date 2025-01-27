package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Sneaker;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.ecommerce.security.JwtUtil;
import com.mycompany.ecommerce.Cart;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;

@Named
@ViewScoped
public class SneakerDetailsBean implements Serializable {

    private Sneaker sneaker;

    @PostConstruct
    public void init() {
        System.out.println("Initializing SneakerDetailsBean...");

        // Получаем ID из параметров запроса
        Map<String, String> params = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap();
        String idParam = params.get("id");

        if (idParam != null && !idParam.isEmpty()) {
            try {
                long sneakerId = Long.parseLong(idParam);
                System.out.println("Fetched sneaker ID from request: " + sneakerId);
                fetchSneakerDetails(sneakerId);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid sneaker ID format: " + idParam);
            }
        } else {
            System.out.println("No sneaker ID found in request parameters.");
        }
    }

    private void fetchSneakerDetails(long id) {
        try {
            URL url = new URL("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + getJwtToken());
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                ObjectMapper mapper = new ObjectMapper();
                this.sneaker = mapper.readValue(connection.getInputStream(), Sneaker.class);
                System.out.println("Sneaker details fetched successfully: " + sneaker.getId());
            } else {
                System.out.println("Failed to fetch sneaker details. Response code: " + responseCode);
            }
        } catch (Exception ex) {
            System.out.println("Error fetching sneaker details: " + ex.getMessage());
        }
    }

    public void deleteSneaker() {
        try {
            URL url = new URL("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/" + sneaker.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Authorization", "Bearer " + getJwtToken());
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Sneaker deleted successfully.");
                this.sneaker = null; // Убираем объект после удаления
            } else {
                System.out.println("Failed to delete sneaker. Response code: " + responseCode);
            }
        } catch (Exception ex) {
            System.out.println("Error deleting sneaker: " + ex.getMessage());
        }
    }
    
    public void updateSneaker(){
        try{
            
                // URL для обновления кроссовка
            URL url = new URL("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/" + sneaker.getId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Authorization", "Bearer " + getJwtToken());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Преобразуем объект Sneaker в JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(sneaker);
        
            // Отправляем JSON в запросе
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получаем ответ от сервера
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Sneaker updated successfully.");
            } 
            else {
                System.out.println("Failed to update sneaker. Response code: " + responseCode);
            }
        }
        catch(Exception ex){
            System.out.println("Error updating sneaker: " + ex.getMessage());
        }
    }
    
    // Добавление кроссовок в корзину
    public String addSneakerToCart() {
        try {
            
            // Получение токена из сессии
            String token = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
            
            // Парсинг id пользователя из токена
            String pureToken = token.replace("Bearer ", "");
            String userId = JwtUtil.parseTokenForId(pureToken);
            
            // Создание и инициализация объекта корзины
            Cart cart = new Cart();
            cart.setUserId(Long.parseLong(userId));
            cart.setSneakerId(this.sneaker.getId());
            
            // Создание json-объекта
            String jsonRequest = String.format("{\"id\":%d, \"userId\":%d, \"sneakerId\":%d}",
            cart.getId(), cart.getUserId(), cart.getSneakerId());

            // Создание клиента и отправка запроса
            Client client = ClientBuilder.newClient();
            Response response = client.target("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart")
                                  .request("application/json")
                                  .header("Authorization", token)
                                  .post(Entity.json(jsonRequest));
            
            if (response.getStatus() == 200) {
                System.out.println("Sneaker successfully added to cart.");
                
                return "";
            } 
            else {
                System.out.println("Error adding to cart. Status: " + response.getStatus());
                
                return "";
            }
        } 
        catch (Exception ex) {
            System.out.println("Error adding to cart. Details: " + ex.getMessage());
            
            return "";
        }
    }
    

    private String getJwtToken() {
        return (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");
    }

    public Sneaker getSneaker() {
        return sneaker;
    }

    public void setSneaker(Sneaker sneaker) {
        this.sneaker = sneaker;
    }
}
