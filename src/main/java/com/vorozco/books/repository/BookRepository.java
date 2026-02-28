package com.vorozco.books.repository;

import com.vorozco.books.model.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD operations on the BOOK table.
 */
@ApplicationScoped
public class BookRepository {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Book save(Book book) {
        if (book.getBookId() == null) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        Book book = findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        em.remove(book);
    }
}
