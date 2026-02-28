package com.vorozco.books.controller;

import com.vorozco.books.model.Author;
import com.vorozco.books.repository.AuthorRepository;
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
 * JAX-RS resource providing CRUD endpoints for the AUTHOR entity.
 *
 * <ul>
 *   <li>GET    /authors         – list all authors</li>
 *   <li>GET    /authors/{id}    – get author by ID</li>
 *   <li>POST   /authors         – create a new author</li>
 *   <li>PUT    /authors/{id}    – update an existing author</li>
 *   <li>DELETE /authors/{id}    – delete an author</li>
 * </ul>
 */
@Path("authors")
public class AuthorResource {

    @Inject
    private AuthorRepository authorRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Author getAuthorById(@PathParam("id") Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAuthor(Author author) {
        Author created = authorRepository.save(author);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Author updateAuthor(@PathParam("id") Long id, Author author) {
        authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        author.setAuthorId(id);
        return authorRepository.save(author);
    }

    @DELETE
    @Path("{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        authorRepository.delete(id);
        return Response.noContent().build();
    }
}
