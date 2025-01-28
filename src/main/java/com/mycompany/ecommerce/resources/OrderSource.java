package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.Order;
import com.mycompany.ecommerce.OrderItem;
import com.mycompany.ecommerce.OrderRepository;
import com.mycompany.ecommerce.filters.Secured;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/order")
public class OrderSource {
    
    @Inject
    private OrderRepository orderRepository;
    
    // Получить список заказов по id пользователя
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/order/{userId}
    @Secured
    @GET
    @Path("{userId}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getByUserId(@PathParam("userId") long userId)
    {
//        try{
//            if(userId == 0){
//                return Response.status(Response.Status.CONFLICT)
//                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
//                    .build();
//            }
//            
//            List<Cart> carts = cartRepository.getCartsByUserId(userId);
//            // Если список cart == null
//            if(carts == null){
//                return Response.status(Response.Status.CONFLICT)
//                    .entity("Не удалось получить список кроссовок в корзине. Параметр userId == null")
//                    .build();
//            }
//            
//            return Response.ok()
//                    .entity(carts)
//                    .build();
//        }
//        catch(Exception ex){
//            return Response.status(Response.Status.CONFLICT)
//                    .entity("Не удалось получить корзину. Детали: " + ex.getMessage())
//                    .build();
//        }
        return null;
    }
    
    // Создать заказ
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/order
    @Secured
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response createOrder(Order order)
    {
        try{
            // Есть параметр cart передан неуспешно
            if(order == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось создать заказ. Параметр order == null")
                    .build();
            }
            
            orderRepository.createOrder(order);
            
            return Response.ok()
                           .entity("Заказ успешно создан")
                           .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity(" Не удоалось добавить кроссовки в корзину. Детали: " + ex.getMessage())
                    .build();
        }
    }
    
    // Создать заказ
    // POST: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/order
    @Secured
    @DELETE
    @Consumes("application/json")
    @Produces("application/json")
    public Response cancelOrder(Order order)
    {
        try{
            // Есть параметр cart передан неуспешно
            if(order == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось создать заказ. Параметр order == null")
                    .build();
            }
            
            orderRepository.createOrder(order);
            
            return Response.ok()
                           .entity("Заказ успешно создан")
                           .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity(" Не удоалось добавить кроссовки в корзину. Детали: " + ex.getMessage())
                    .build();
        }
    }
}
