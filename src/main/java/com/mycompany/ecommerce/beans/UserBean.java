package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.security.JwtUtil;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.Serializable;
import java.net.URI;

// import org.primefaces.model.file.UploadedFile;

@Named
@RequestScoped
public class UserBean implements Serializable {
    // Имя пользователя
    private String name;
    // Почта пользователя
    private String email;
    // Пароль пользователя
    private String password;
    // id пользователя
    private long id;
    // Аватар пользователя 
    private Part avatarPart;
    
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

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }
    
    public Part getAvatarPart() {
        return avatarPart;
    }
    public void setAvatarPart(Part avatarPart) {
        this.avatarPart = avatarPart;
    }
    
    
    
    // Вход пользователя в аккаунт
    public String login() {
        try {
            Client client = ClientBuilder.newClient();
            URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/login");

            // Создаем JSON-запрос
            String jsonRequest = String.format("{\"email\":\"%s\", \"password\":\"%s\"}", email, password);

            Response response = client.target(uri)
                .request("application/json")
                .post(Entity.json(jsonRequest));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                String token = response.getHeaderString("Authorization");
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Login successful", null));
                // Сохранить токен в сессии или localStorage (если нужно)
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("jwtToken", token);
                return "home.xhtml?faces-redirect=true"; // Перенаправление на главную страницу
            } 
            else {
                String errorMessage = response.readEntity(String.class);
                FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
                return null;
            }
        
        } 
        catch (Exception ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed. Please try again.", null));
            return null;
        }
    }
    
    // Регистрация пользователя 
    public String register() {
        try {
            // URL вашего API
            String apiUrl = "http://localhost:8090/ECommerce-Store-Java/api/auth/register";

            String avatarUrl = null;
           
            // Если аватар был загружен
            if (avatarPart != null && avatarPart.getSubmittedFileName() != null) {
                String fileName = avatarPart.getSubmittedFileName();
                
                // Путь к папке uploads
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String uploadsDir = servletContext.getRealPath("/uploads");
                
                // Сохраняем файл на сервере
                Path filePath = Paths.get(uploadsDir, fileName);
                Files.copy(avatarPart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Формируем URL для сохранения в базе (относительный путь для доступа через сервер)
                avatarUrl = "uploads/" + fileName;
            }
            
            // Создаем JSON-запрос
            String jsonRequest = String.format(
                    "{\"name\":\"%s\", \"email\":\"%s\", \"password\":\"%s\",\"avatarUrl\":\"%s\"}",
                    name, email, password, avatarUrl
            );

            // Отправляем запрос
            Client client = ClientBuilder.newClient();
            Response response = client.target(apiUrl)
                    .request("application/json")
                    .post(Entity.json(jsonRequest));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                // Успешная регистрация
                // Сохранить токен в сессии или localStorage (если нужно)
                String token = response.getHeaderString("Authorization");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("jwtToken", token);
                return "home.xhtml?faces-redirect=true"; // Перенаправление на главную страницу
            } 
            else {
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
