package com.vorozco.books.repository;

import com.vorozco.books.model.BookAuthor;
import com.vorozco.books.model.BookAuthorId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD operations on the BOOK_AUTHOR associative table.
 */
@ApplicationScoped
public class BookAuthorRepository {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public List<BookAuthor> findByBookId(Long bookId) {
        return em.createQuery(
                        "SELECT ba FROM BookAuthor ba WHERE ba.id.bookId = :bookId", BookAuthor.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public Optional<BookAuthor> findById(BookAuthorId id) {
        return Optional.ofNullable(em.find(BookAuthor.class, id));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public BookAuthor save(BookAuthor bookAuthor) {
        return em.merge(bookAuthor);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(BookAuthorId id) {
        BookAuthor ba = findById(id)
                .orElseThrow(() -> new NotFoundException("BookAuthor not found for id: " + id));
        em.remove(ba);
    }
}
