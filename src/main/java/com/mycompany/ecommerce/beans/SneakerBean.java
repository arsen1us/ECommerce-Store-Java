package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Sneaker;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.util.List;
import java.net.URI;

@Named
@RequestScoped
public class SneakerBean {
    private List<Sneaker> sneakers;

    @PostConstruct
    public void init() {
        loadSneakers();
    }

    public void loadSneakers() {
        try {
            // Получение токена из сессии
            String token = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");

            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker");
            
            // Отправка GET-запроса
            WebTarget target = client.target(uri);
            Response response = target.request("application/json")
                .header("Authorization", "Bearer " + token)
                .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешный ответ
                sneakers = response.readEntity(new GenericType<List<Sneaker>>() {});
            } else {
                // Обработка ошибки
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка загрузки кроссовок", errorMessage));
            }
        } catch (Exception e) {
            // Логирование и уведомление об ошибке
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка соединения с API", e.getMessage()));
        } 
    }

    public List<Sneaker> getSneakers() {
        return sneakers;
    }
}
