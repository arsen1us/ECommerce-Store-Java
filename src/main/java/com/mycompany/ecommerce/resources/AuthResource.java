package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.User;
import com.mycompany.ecommerce.UserRepository;
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

    @POST
    @Path("/register")
    @Consumes("application/json")
    @Produces("application/json")
    public Response register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
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
        userRepository.save(user);

        return Response.status(Response.Status.CREATED).entity("User registered successfully.").build();
    }

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(User user) {
        var existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isEmpty() || !BCrypt.checkpw(user.getPassword(), existingUser.get().getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password.")
                    .build();
        }

        // В реальной системе: генерировать JWT токен
        return Response.ok("Login successful").build();
    }
}
