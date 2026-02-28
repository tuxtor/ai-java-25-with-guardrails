package com.vorozco.books.repository;

import com.vorozco.books.model.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Repository for CRUD operations on the CATEGORY table.
 */
@ApplicationScoped
public class CategoryRepository {

    @PersistenceContext(unitName = "pu")
    private EntityManager em;

    public List<Category> findAll() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(em.find(Category.class, id));
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public Category save(Category category) {
        if (category.getCategoryId() == null) {
            em.persist(category);
            return category;
        }
        return em.merge(category);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Long id) {
        Category category = findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        em.remove(category);
    }
}
