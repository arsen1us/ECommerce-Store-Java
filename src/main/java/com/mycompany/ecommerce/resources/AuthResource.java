package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.User;
import com.mycompany.ecommerce.UserRepository;
import com.mycompany.ecommerce.security.JwtUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import com.mycompany.ecommerce.filters.Secured;

@Stateless
@Path("/auth")
public class AuthResource {

    @Inject
    private UserRepository userRepository;
    
    // Получить пользователя по id
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/{id}
    @Secured
    @GET
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getById(@PathParam("id") long id)
    {
        try{
            var existingUser = userRepository.findById(id);
            
            // Если пользователь не найден
            if(existingUser == null){
                return Response.status(404)
                        .entity("User with id " + id + " not found")
                        .build();
            }
            // Если найден
            return Response.ok()
                    .entity(existingUser)
                    .build();
        }
        catch(Exception ex){
            System.out.println("Произошла ошибка во получения пользователя по id. Детали: " + ex.getMessage());
            
            return Response.status(Response.Status.CONFLICT)
                    .entity(ex.getMessage())
                    .build();
        }
    }
    
    // Регистрация пользователя 
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/register
    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(User user)
    {
        try{
            // Проверка, не занята ли данная почта
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                return Response.status(Response.Status.CONFLICT)
                    .entity("Email is already registered.")
                    .build();
            }
            
            // Сохранение
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);

            // Генерация токена
            String token = JwtUtil.generateToken(
                    user.getEmail(),
                    user.getId(),
                    user.getRole());
            
            // Успешный ответ
            return Response.ok()
                        .header("Authorization", "Bearer " + token)
                        .entity(user)
                        .build();
        }
        catch (Exception ex){
            System.out.println("Произошла ошибка во время регистрации. Детали: " + ex.getMessage());
            
            return Response.status(Response.Status.CONFLICT)
                    .entity(ex.getMessage())
                    .build();
        }
    }
    
    // Аутентификация пользователя 
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/auth/register
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
            // Генерация токена
            String token = JwtUtil.generateToken(
                    existingUser.get().getEmail(),
                    existingUser.get().getId(),
                    existingUser.get().getRole());
            
            return Response.ok()
                        .header("Authorization", "Bearer " + token)
                        .entity("Login successful")
                        .build();   
        }
        catch(Exception ex){
            System.out.println("Произошла ошибка во время входа в систему. Детали: " + ex.getMessage());
            
            return Response.status(Response.Status.CONFLICT)
                    .entity(ex.getMessage())
                    .build();
        }
    }
}
