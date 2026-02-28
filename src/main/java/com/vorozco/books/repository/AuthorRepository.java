package com.vorozco.books.repository;

import com.vorozco.books.model.Author;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD operations on the AUTHOR table.
 */
@ApplicationScoped
public class AuthorRepository {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public List<Author> findAll() {
        return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }

    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Author save(Author author) {
        if (author.getAuthorId() == null) {
            em.persist(author);
            return author;
        }
        return em.merge(author);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        Author author = findById(id)
                .orElseThrow(() -> new NotFoundException("Author not found with id: " + id));
        em.remove(author);
    }
}
