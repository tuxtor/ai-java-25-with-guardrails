package com.vorozco.books;

import org.testcontainers.oracle.OracleContainer;

/**
 * Abstract base class that starts a shared Oracle Free (slim) Testcontainer once
 * for all IT subclasses. The static initialiser runs before any JUnit extension
 * callbacks (including {@code @HelidonTest}'s {@code HelidonJunitExtension}),
 * so the system properties are already set when Helidon bootstraps its datasource.
 */
public abstract class AbstractOracleIT {

    static final OracleContainer ORACLE;

    static {
        ORACLE = new OracleContainer("gvenzl/oracle-free:slim-faststart");
        ORACLE.start();

        // Override datasource config so Helidon/HikariCP connects to the container.
        System.setProperty("javax.sql.DataSource.ds.dataSourceClassName",
                "oracle.jdbc.pool.OracleDataSource");
        System.setProperty("javax.sql.DataSource.ds.dataSource.url",
                ORACLE.getJdbcUrl());
        System.setProperty("javax.sql.DataSource.ds.dataSource.user",
                ORACLE.getUsername());
        System.setProperty("javax.sql.DataSource.ds.dataSource.password",
                ORACLE.getPassword());
    }
}
