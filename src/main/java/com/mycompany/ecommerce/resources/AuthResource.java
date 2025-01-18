package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.User;
import com.mycompany.ecommerce.UserRepository;
import com.mycompany.ecommerce.security.JwtUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;

@Stateless
@Path("/auth")
public class AuthResource {

    @Inject
    private UserRepository userRepository;
    // Регистрация пользователя 
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/register
    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(User user)
    {
        try{
            if (userRepository.findByUsername(user.getName()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Username is already taken.")
                    .build();
            }

            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Email is already registered.")
                    .build();
            }
            
            // Хэширование пароля перед сохранением
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
            // Сохранение
            userRepository.save(user);

            // Генерация токена
            String token = JwtUtil.generateToken(user.getEmail());
            
            // Успешный ответ
            return Response.ok()
                        .header("Authorization", "Bearer " + token)
                        .entity("Login successful")
                        .build();
        }
        catch (Exception ex){
            System.out.println("Произошла ошибка во время регистрации. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
    
    // Аутентификация пользователя 
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/register
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(User user) {
        try{
            var existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser.isEmpty() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password.")
                    .build();
            }
            
            String token = JwtUtil.generateToken(existingUser.get().getEmail());
            
            // В реальной системе: генерировать JWT токен
            return Response.ok()
                        .header("Authorization", "Bearer " + token)
                        .entity("Login successful")
                        .build();   
        }
        catch(Exception ex){
            System.out.println("Произошла ошибка во время входа в систему. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
