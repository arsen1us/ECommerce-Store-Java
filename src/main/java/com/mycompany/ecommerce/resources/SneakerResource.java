package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.Sneaker;
import com.mycompany.ecommerce.SneakerRepository;
import com.mycompany.ecommerce.security.JwtUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

@Stateless
@Path("/sneaker")
public class SneakerResource {

    @Inject
    private SneakerRepository sneakerRepository;
    // Получить весь список кроссовок
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public Response getAll()
    {
        try{
            
            List<Sneaker> allSneakers = sneakerRepository.getAllSneakers();
            
            return Response.ok()
                    .entity(allSneakers)
                    .build();
            
        }
        catch (Exception ex){
            System.out.println("Не удалось получить список кроссовок. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .build();
        }
    }
    
    // Получить кроссовки по id
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/{id}
    @GET
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getById(@PathParam("id") long id) {
        try{
            var sneaker = sneakerRepository.getSneakerById(id);
            
            return Response.ok()
                   .entity(sneaker)
                   .build();
        }
        catch(Exception ex){
            System.out.println("Не удалось получить кроссовки по id. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .build();
        }
    }
    
    // Добавить новые кроссовки
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(Sneaker sneaker) {
        try{
            var updatedSneaker = sneakerRepository.addSneaker(sneaker);
            
            return Response.ok()
                   .entity(updatedSneaker)
                   .build();
        }
        catch(Exception ex){
            System.out.println("Не удалось получить кроссовки по id. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .build();
        }
    }
    
    // Изменить кроссовки
    // PUT: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/{id}
    @PUT
    @Path("/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response update(@PathParam("id") long id, Sneaker sneaker) {
        try{
            Sneaker existingSneaker = sneakerRepository.getSneakerById(id);
            
            // Обновление полей
            existingSneaker.setName(sneaker.getName());
            existingSneaker.setBrand(sneaker.getBrand());
            existingSneaker.setPrice(sneaker.getPrice());
            existingSneaker.setSize(sneaker.getSize());
            existingSneaker.setColor(sneaker.getColor());
            existingSneaker.setCount(sneaker.getCount());
            existingSneaker.setAvailable(sneaker.getAvailable());
            
            // Обновление 
            Sneaker savedSneaker = sneakerRepository.updateSneaker(existingSneaker);
            
            return Response.ok()
                           .entity(savedSneaker)
                           .build();
        }
        catch(Exception ex){
            System.out.println("Не удалось изменить кроссовки по id. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .build();
        }
    }
    
    // Удалить кросовки по id
    // DELETE: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/sneaker/{id}
    @DELETE
    @Path("{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response delete(@PathParam("id") long id) {
        try{
            System.out.print("123123123");
            // Удалить
            sneakerRepository.deleteSneakerById(id);
            return Response.ok()
                           .entity("Кроссовки с id успешно удалены")
                           .build();
        }
        catch(Exception ex){
            System.out.println("Не удалось удалить кроссовки по id. Детали: " + ex.getMessage());
            return Response.status(Response.Status.CONFLICT)
                           .build();
        }
    }
    
}
