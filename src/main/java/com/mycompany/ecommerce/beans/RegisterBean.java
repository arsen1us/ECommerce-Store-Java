package com.mycompany.ecommerce.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;

@Named
@RequestScoped
public class RegisterBean implements Serializable {

    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String register() {
        try {
            // URL вашего API
            String apiUrl = "http://localhost:8090/ECommerce-Store-Java/api/auth/register";

            // Создаем JSON-запрос
            String jsonRequest = String.format(
                    "{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\"}",
                    name, email, password
            );

            // Отправляем запрос
            Client client = ClientBuilder.newClient();
            Response response = client.target(apiUrl)
                    .request("application/json")
                    .post(Entity.json(jsonRequest));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешная регистрация
                return "login.xhtml?faces-redirect=true"; // Перенаправление на страницу логина
            } else {
                // Обработка ошибок
                String errorMessage = response.readEntity(String.class);
                throw new RuntimeException(errorMessage);
            }
        } catch (Exception e) {
            // Сообщение об ошибке (для демонстрации)
            e.printStackTrace();
            return null;
        }
    }
}
