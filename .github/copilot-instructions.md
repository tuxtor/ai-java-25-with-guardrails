# GitHub Copilot Instructions

## Project Overview

This is **ai-java-25-books**, a [Helidon MicroProfile](https://helidon.io) REST application built with Java 25. It exposes JAX-RS endpoints backed by JPA/Hibernate and an H2 in-memory database (with optional Oracle DB support).

## Tech Stack

- **Java 25**
- **Helidon MicroProfile 4.x** (JAX-RS, CDI, JPA, Health, Metrics)
- **Hibernate ORM** (JPA provider)
- **H2** (in-memory database for testing; Oracle DB for production)
- **Maven** (build tool)
- **JUnit 5 + Helidon MicroProfile Testing** (test framework)

## Project Structure

```
src/
  main/
    java/com/vorozco/books/   # Application source (resources, entities, etc.)
    resources/META-INF/       # persistence.xml, microprofile-config.properties, SQL init scripts
  test/
    java/com/vorozco/books/   # JUnit 5 tests using @HelidonTest
    resources/                # Test configuration overrides
```

## Coding Conventions

- Package root: `com.vorozco.books`
- JAX-RS resource classes end with `Resource` (e.g., `PokemonResource`, `PokemonTypeResource`)
- JPA entity classes are plain POJOs annotated with `@Entity` and `@Table`
- Use CDI `@Inject` for dependency injection; avoid `new` for managed beans
- Use Jakarta EE APIs (`jakarta.*`) — not `javax.*`
- Prefer records for immutable data transfer objects when applicable (Java 25 feature)

## Build & Test

```bash
# Build and run all tests
mvn verify

# Package only (skip tests)
mvn package -DskipTests

# Run the application
java -jar target/ai-java-25-books.jar
```

## Key Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/simple-greet` | Plain text greeting |
| GET/PUT | `/greet`, `/greet/{name}` | JSON greeting |
| GET/POST/DELETE | `/pokemon` | Pokemon CRUD |
| GET | `/type` | Pokemon types |
| GET | `/health` | MicroProfile Health check |
| GET | `/metrics` | MicroProfile Metrics |

## Guidelines for Copilot Suggestions

- Prefer **Java 25** language features (records, pattern matching, sealed classes, virtual threads) where appropriate
- Follow **MicroProfile** and **Jakarta EE** standards; avoid Spring-specific patterns
- Keep database access through **JPA/EntityManager**; avoid raw JDBC unless necessary
- Write tests using `@HelidonTest` with JUnit 5 and Hamcrest matchers
- Configuration properties belong in `src/main/resources/META-INF/microprofile-config.properties`
