package com.vorozco.books.controller;

import com.vorozco.books.model.Category;
import com.vorozco.books.repository.CategoryRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * JAX-RS resource providing CRUD endpoints for the CATEGORY entity.
 *
 * <ul>
 *   <li>GET    /categories        – list all categories</li>
 *   <li>GET    /categories/{id}   – get category by ID</li>
 *   <li>POST   /categories        – create a new category</li>
 *   <li>PUT    /categories/{id}   – update an existing category</li>
 *   <li>DELETE /categories/{id}   – delete a category</li>
 * </ul>
 */
@Path("categories")
public class CategoryResource {

    @Inject
    private CategoryRepository categoryRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Category getCategoryById(@PathParam("id") Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(Category category) {
        Category created = categoryRepository.save(category);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Category updateCategory(@PathParam("id") Long id, Category category) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        category.setCategoryId(id);
        return categoryRepository.save(category);
    }

    @DELETE
    @Path("{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        categoryRepository.delete(id);
        return Response.noContent().build();
    }
}
