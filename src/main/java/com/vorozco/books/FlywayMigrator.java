package com.vorozco.books;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;

import jakarta.inject.Inject;

/**
 * CDI bean that runs Flyway schema migrations when the application context starts.
 * This ensures the database schema is up-to-date before any JPA operations occur.
 */
@ApplicationScoped
public class FlywayMigrator {

    @Inject
    @ConfigProperty(name = "javax.sql.DataSource.ds.dataSource.url")
    private String jdbcUrl;

    @Inject
    @ConfigProperty(name = "javax.sql.DataSource.ds.dataSource.user")
    private String user;

    @Inject
    @ConfigProperty(name = "javax.sql.DataSource.ds.dataSource.password")
    private String password;

    public void onStart(@Observes @Initialized(ApplicationScoped.class) Object event) {
        Flyway.configure()
                .dataSource(jdbcUrl, user, password)
                .load()
                .migrate();
    }
}
