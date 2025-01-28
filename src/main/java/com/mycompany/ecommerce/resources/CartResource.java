package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.Cart;
import com.mycompany.ecommerce.CartRepository;
import com.mycompany.ecommerce.filters.Secured;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/cart")
public class CartResource {
    
    @Inject
    private CartRepository cartRepository;
    
    // Получить корзину по id пользователя
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/{userId}
    @Secured
    @GET
    @Path("{userId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getByUserId(@PathParam("userId") long userId)
    {
        try{
            if(userId == 0){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
                    .build();
            }
            
            List<Cart> carts = cartRepository.getCartsByUserId(userId);
            // Если список cart == null
            if(carts == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
                    .build();
            }
            
            return Response.ok()
                    .entity(carts)
                    .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить корзину. Детали: " + ex.getMessage())
                    .build();
        }
    }
    
    // Получить стоимость кроссовок в корзине по id пользователя
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/price/{userId}
    @Secured
    @GET
    @Path("price/{userId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getCartPriceByUserId(@PathParam("userId") long userId)
    {
        try{
            if(userId == 0){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
                    .build();
            }
            
            List<Cart> carts = cartRepository.getCartsByUserId(userId);
            // Если список cart == null
            if(carts == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
                    .build();
            }
            
            // Получить список id кроссовок
            List<Long> sneakerIds = carts.stream()
                .map(cart -> cart.getSneakerId())
                .collect(Collectors.toList());
            
            // Итоговая стоимость
            double totalPrice = cartRepository.calculatePriceBySneakerIds(sneakerIds);
            
            return Response.ok()
                    .entity(totalPrice)
                    .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить корзину. Детали: " + ex.getMessage())
                    .build();
        }
    }
    
    // Добавить кроссовки в корзину
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/cart/
    @Secured
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response add(Cart cart)
    {
        try{
            // Есть параметр cart передан неуспешно
            if(cart == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось добавить кроссовки в корзину. Параметр cart == null")
                    .build();
            }
            
            Cart addedCart = cartRepository.addCart(cart);
            return Response.ok()
                           .entity(addedCart)
                           .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity(" Не удоалось добавить кроссовки в корзину. Детали: " + ex.getMessage())
                    .build();
        }
    }
    
}
