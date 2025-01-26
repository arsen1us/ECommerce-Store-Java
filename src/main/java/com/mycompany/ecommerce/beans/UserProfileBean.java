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
import com.mycompany.ecommerce.security.JwtUtil;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import java.net.http.HttpClient;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

@Named
@ViewScoped
public class UserProfileBean implements Serializable {
    
    // id пользователя
    private long id;
    // name пользователя
    private String name;
    // email пользователя
    private String email;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
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
    
    @PostConstruct
    public void init() {
        // Загрузить информацию о пользователе
        loadUserProfile();
    }
    
    // Загрузить профиль пользователя
    public void loadUserProfile() {
        try {
            // Получение jwt-токена
            String token = (String) FacesContext.getCurrentInstance()
                                            .getExternalContext()
                                            .getSessionMap()
                                            .get("jwtToken");
            if (token == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authorization token is missing.", null));
                return;
            }
        
            String pureToken = token.replace("Bearer ", "");
        
            String userId = JwtUtil.parseTokenForId(pureToken);
        
            if (userId == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to extract user ID from token.", null));
                return;
            }
        
            String apiUrl = "http://localhost:8090/ECommerce-Store-Java/api/auth/" + userId;
            Client client = ClientBuilder.newClient();
            Response response = client.target(apiUrl)
                .request("application/json")
                // Добавить токен в заголовок Authorization
                .header("Authorization", token)
                .get();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                UserBean user = response.readEntity(UserBean.class);
                this.name = user.getName();
                this.email = user.getEmail();
                this.id = user.getId();
            } else {
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error loading profile: " + e.getMessage(), null));
        }
    }
}
