package com.vorozco.books.repository;

import com.vorozco.books.model.BookCategory;
import com.vorozco.books.model.BookCategoryId;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD operations on the BOOK_CATEGORY associative table.
 */
@ApplicationScoped
public class BookCategoryRepository {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public List<BookCategory> findByBookId(Long bookId) {
        return em.createQuery(
                        "SELECT bc FROM BookCategory bc WHERE bc.id.bookId = :bookId", BookCategory.class)
                .setParameter("bookId", bookId)
                .getResultList();
    }

    public Optional<BookCategory> findById(BookCategoryId id) {
        return Optional.ofNullable(em.find(BookCategory.class, id));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public BookCategory save(BookCategory bookCategory) {
        return em.merge(bookCategory);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(BookCategoryId id) {
        BookCategory bc = findById(id)
                .orElseThrow(() -> new NotFoundException("BookCategory not found for id: " + id));
        em.remove(bc);
    }
}
