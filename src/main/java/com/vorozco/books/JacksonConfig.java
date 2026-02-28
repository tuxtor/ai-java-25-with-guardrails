package com.vorozco.books;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

/**
 * JAX-RS provider that configures the shared Jackson {@link ObjectMapper} with
 * Java 8 date/time support (JSR-310 module) and disables writing dates as
 * timestamps, so {@code LocalDate} / {@code LocalDateTime} fields in entities
 * are serialised as ISO-8601 strings.
 */
@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
