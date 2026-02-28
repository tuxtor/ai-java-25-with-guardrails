package com.vorozco.books;

import com.vorozco.books.model.Author;
import com.vorozco.books.model.Book;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Integration tests for the books CRUD REST layer.
 * Uses an Oracle Free (slim) Testcontainer started in {@link AbstractOracleIT}.
 * Test data is drawn from Guatemalan classic literature.
 */
@HelidonTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookIT extends AbstractOracleIT {

    @Inject
    private WebTarget target;

    // -----------------------------------------------------------------------
    // Author lifecycle (needed so we can link books to authors later)
    // -----------------------------------------------------------------------

    @Test
    @Order(1)
    void createMiguelAngelAsturias() {
        Author author = new Author();
        author.setGivenName("Miguel Ángel");
        author.setFamilyName("Asturias");
        author.setDisplayName("Miguel Ángel Asturias");
        author.setBirthDate(LocalDate.of(1899, 10, 19));
        author.setDeathDate(LocalDate.of(1974, 6, 9));
        author.setBiography("Guatemalan novelist, poet and diplomat. Nobel Prize in Literature 1967.");

        Response response = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(author));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getJsonNumber("authorId").longValue(), greaterThan(0L));
        assertThat(created.getString("displayName"), is("Miguel Ángel Asturias"));
    }

    @Test
    @Order(2)
    void createElSenorPresidente() {
        Book book = new Book();
        book.setIsbn("978-84-376-0494-7");
        book.setTitle("El Señor Presidente");
        book.setSubtitle(null);
        book.setDescription("A novel depicting the cruel despotism of a Latin American dictator.");
        book.setPublicationDate(LocalDate.of(1946, 1, 1));
        book.setEdition("1st");
        book.setLanguageCode("es");

        Response response = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(book));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getJsonNumber("bookId").longValue(), greaterThan(0L));
        assertThat(created.getString("title"), is("El Señor Presidente"));
    }

    @Test
    @Order(3)
    void createHombresDeMaiz() {
        Book book = new Book();
        book.setIsbn("978-84-376-0620-0");
        book.setTitle("Hombres de Maíz");
        book.setDescription("A mythological novel exploring the struggle of indigenous Guatemalan people.");
        book.setPublicationDate(LocalDate.of(1949, 1, 1));
        book.setEdition("1st");
        book.setLanguageCode("es");

        Response response = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(book));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getString("title"), is("Hombres de Maíz"));
    }

    @Test
    @Order(4)
    void listBooksReturnsAtLeastTwo() {
        JsonArray books = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        assertThat(books.size(), greaterThan(1));
    }

    @Test
    @Order(5)
    void getBookByIdReturnsCorrectTitle() {
        // Retrieve all books and pick the first one
        JsonArray books = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        long firstId = books.getJsonObject(0).getJsonNumber("bookId").longValue();

        JsonObject fetched = target.path("books/" + firstId)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertThat(fetched.getJsonNumber("bookId").longValue(), is(firstId));
        assertThat(fetched.getString("title"), notNullValue());
    }

    @Test
    @Order(6)
    void updateBookEdition() {
        JsonArray books = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        JsonObject first = books.getJsonObject(0);
        long bookId = first.getJsonNumber("bookId").longValue();

        Book update = new Book();
        update.setIsbn(first.isNull("isbn") ? null : first.getString("isbn"));
        update.setTitle(first.getString("title"));
        update.setLanguageCode("es");
        update.setEdition("2nd");

        JsonObject updated = target.path("books/" + bookId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(update), JsonObject.class);

        assertThat(updated.getString("edition"), is("2nd"));
    }

    @Test
    @Order(7)
    void deleteBookReducesCount() {
        JsonArray before = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        int sizeBefore = before.size();
        long lastId = before.getJsonObject(sizeBefore - 1).getJsonNumber("bookId").longValue();

        Response deleteResponse = target.path("books/" + lastId)
                .request()
                .delete();

        assertThat(deleteResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));

        JsonArray after = target.path("books")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        assertThat(after.size(), is(sizeBefore - 1));
    }

    @Test
    @Order(8)
    void getNotFoundBookReturns404() {
        Response response = target.path("books/999999")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }
}
