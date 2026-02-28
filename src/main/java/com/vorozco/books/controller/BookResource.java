package com.vorozco.books.controller;

import com.vorozco.books.model.Book;
import com.vorozco.books.repository.BookRepository;
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
 * JAX-RS resource providing CRUD endpoints for the BOOK entity.
 *
 * <ul>
 *   <li>GET    /books        – list all books</li>
 *   <li>GET    /books/{id}   – get book by ID</li>
 *   <li>POST   /books        – create a new book</li>
 *   <li>PUT    /books/{id}   – update an existing book</li>
 *   <li>DELETE /books/{id}   – delete a book</li>
 * </ul>
 */
@Path("books")
public class BookResource {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Book getBookById(@PathParam("id") Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBook(Book book) {
        Book created = bookRepository.save(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Book updateBook(@PathParam("id") Long id, Book book) {
        bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        book.setBookId(id);
        return bookRepository.save(book);
    }

    @DELETE
    @Path("{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }
}
