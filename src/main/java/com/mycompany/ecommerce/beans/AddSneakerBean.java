package com.mycompany.ecommerce.beans;

import com.mycompany.ecommerce.Sneaker;
import com.mycompany.ecommerce.SneakerRepository;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;
import jakarta.servlet.ServletContext;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Named
@RequestScoped
public class AddSneakerBean {
    
    @Inject
    private SneakerRepository _sneakerRepository;
    
    private String name;
    private String brand;
    private double price;
    private String size;
    private String color;
    private int count;
    private boolean available;
    private Part sneakerImagePart;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Part getSneakerImagePart(){
        return this.sneakerImagePart;
    }
    public void setSneakerImagePart(Part sneakerImagePart){
        this.sneakerImagePart = sneakerImagePart;
    }
        
    public void addSneaker() {
        try {
        // Получаем JWT токен из сессии
        String jwtToken = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("jwtToken");

        if (jwtToken == null || jwtToken.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Authentication token is missing. Please log in.", null));
            return;
        }

        String sneakerImageUrl = null;
           
            // Если аватар был загружен
            if (sneakerImagePart != null && sneakerImagePart.getSubmittedFileName() != null) {
                String fileName = sneakerImagePart.getSubmittedFileName();
                
                // Путь к папке uploads
                ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String uploadsDir = servletContext.getRealPath("/uploads");
                
                // Сохраняем файл на сервере
                Path filePath = Paths.get(uploadsDir, fileName);
                Files.copy(sneakerImagePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                sneakerImageUrl = "uploads/" + fileName;
            }
        
        
        // Создаем HTTP-клиент
        Client client = ClientBuilder.newClient();
        URI uri = new URI("http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker");

        // Создаем объект Sneaker и заполняем его данными
        Sneaker sneaker = new Sneaker();
        sneaker.setName(name);
        sneaker.setBrand(brand);
        sneaker.setPrice(price);
        sneaker.setSize(size);
        sneaker.setColor(color);
        sneaker.setCount(count);
        sneaker.setAvailable(available);
        sneaker.setImageUrl(sneakerImageUrl);

        // Отправляем POST-запрос с объектом Sneaker в формате JSON, включая заголовок авторизации
        Response response = client.target(uri)
            .request("application/json")
            .header("Authorization", "Bearer " + jwtToken) // Добавляем JWT токен
            .post(Entity.json(sneaker));

        // Проверяем статус ответа
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sneaker added successfully!", null));
        }   else {
            String errorMessage = response.readEntity(String.class);
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
            }
        }
        catch (Exception e) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed to add sneaker: " + e.getMessage(), null));
        }
    }
}

