package com.mycompany.ecommerce.resources;

import com.mycompany.ecommerce.CategoryRepository;
import com.mycompany.ecommerce.Category;
import com.mycompany.ecommerce.filters.Secured;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Stateless
@Path("/category")
public class CategoryResource {
    
    @Inject
    private CategoryRepository categoryRepository;
    
    // Получить список все категорий
    // GET: http://desktop-9rtlih5:8090/ECommerce-Store-Java/api/category
    @Secured
    @GET
    @Consumes("application/json")
    @Produces("application/json")
    public Response getAll()
    {
        try{
            List<Category> categories = categoryRepository.getAll();
            
            // Если список cart == null
            if(categories == null){
                return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список категорий")
                    .build();
            }
            
            return Response.ok()
                    .entity(categories)
                    .build();
        }
        catch(Exception ex){
            return Response.status(Response.Status.CONFLICT)
                    .entity("Не удалось получить список категорий. Детали: " + ex.getMessage())
                    .build();
        }
    }
}
