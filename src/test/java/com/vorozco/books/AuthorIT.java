package com.vorozco.books;

import com.vorozco.books.model.Author;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.datafaker.Faker;
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
 * Integration tests for the authors CRUD REST layer.
 * Uses DataFaker for generating realistic supplementary test data alongside
 * real Guatemalan authors.
 */
@HelidonTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorIT extends AbstractOracleIT {

    private static final Faker FAKER = new Faker();

    @Inject
    private WebTarget target;

    @Test
    @Order(1)
    void createRafaelArevaloMartinez() {
        Author author = new Author();
        author.setGivenName("Rafael");
        author.setFamilyName("Arévalo Martínez");
        author.setDisplayName("Rafael Arévalo Martínez");
        author.setBirthDate(LocalDate.of(1884, 7, 25));
        author.setDeathDate(LocalDate.of(1975, 6, 12));
        author.setBiography("Guatemalan modernist poet, novelist and short-story writer. "
                + "Best known for 'El hombre que parecía un caballo'.");

        Response response = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(author));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getJsonNumber("authorId").longValue(), greaterThan(0L));
        assertThat(created.getString("familyName"), is("Arévalo Martínez"));
    }

    @Test
    @Order(2)
    void createLuisCardozaYAragon() {
        Author author = new Author();
        author.setGivenName("Luis");
        author.setFamilyName("Cardoza y Aragón");
        author.setDisplayName("Luis Cardoza y Aragón");
        author.setBirthDate(LocalDate.of(1904, 6, 21));
        author.setDeathDate(LocalDate.of(1992, 9, 4));
        author.setBiography("Guatemalan poet and essayist, key figure in Latin American surrealism.");

        Response response = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(author));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getString("displayName"), is("Luis Cardoza y Aragón"));
    }

    @Test
    @Order(3)
    void createFakeAuthorWithDataFaker() {
        Author author = new Author();
        author.setGivenName(FAKER.name().firstName());
        author.setFamilyName(FAKER.name().lastName());
        author.setDisplayName(FAKER.name().fullName());
        author.setBiography(FAKER.lorem().paragraph(2));

        Response response = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(author));

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        JsonObject created = response.readEntity(JsonObject.class);
        assertThat(created.getJsonNumber("authorId").longValue(), greaterThan(0L));
        assertThat(created.getString("givenName"), notNullValue());
    }

    @Test
    @Order(4)
    void listAuthorsReturnsAtLeastTwo() {
        JsonArray authors = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        assertThat(authors.size(), greaterThan(1));
    }

    @Test
    @Order(5)
    void getAuthorByIdReturnsCorrectData() {
        JsonArray authors = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        long firstId = authors.getJsonObject(0).getJsonNumber("authorId").longValue();

        JsonObject fetched = target.path("authors/" + firstId)
                .request(MediaType.APPLICATION_JSON)
                .get(JsonObject.class);

        assertThat(fetched.getJsonNumber("authorId").longValue(), is(firstId));
        assertThat(fetched.getString("familyName"), notNullValue());
    }

    @Test
    @Order(6)
    void updateAuthorBiography() {
        JsonArray authors = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        JsonObject first = authors.getJsonObject(0);
        long authorId = first.getJsonNumber("authorId").longValue();
        String updatedBio = FAKER.lorem().sentence(10);

        Author update = new Author();
        update.setGivenName(first.getString("givenName"));
        update.setFamilyName(first.getString("familyName"));
        update.setDisplayName(first.isNull("displayName") ? null : first.getString("displayName"));
        update.setBiography(updatedBio);

        JsonObject updated = target.path("authors/" + authorId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(update), JsonObject.class);

        assertThat(updated.getString("biography"), is(updatedBio));
    }

    @Test
    @Order(7)
    void deleteAuthorReducesCount() {
        JsonArray before = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        int sizeBefore = before.size();
        long lastId = before.getJsonObject(sizeBefore - 1).getJsonNumber("authorId").longValue();

        Response deleteResponse = target.path("authors/" + lastId)
                .request()
                .delete();

        assertThat(deleteResponse.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));

        JsonArray after = target.path("authors")
                .request(MediaType.APPLICATION_JSON)
                .get(JsonArray.class);

        assertThat(after.size(), is(sizeBefore - 1));
    }

    @Test
    @Order(8)
    void getNotFoundAuthorReturns404() {
        Response response = target.path("authors/999999")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }
}
