package com.vorozco.books.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * JPA entity representing the BOOK_CATEGORY associative table.
 * Resolves the many-to-many relationship between BOOK and CATEGORY.
 */
@Entity(name = "BookCategory")
@Table(name = "BOOK_CATEGORY")
@Access(AccessType.FIELD)
public class BookCategory {

    @EmbeddedId
    private BookCategoryId id;

    @Column(name = "IS_PRIMARY")
    private Boolean isPrimary;

    public BookCategory() {
    }

    public BookCategory(BookCategoryId id) {
        this.id = id;
    }

    public BookCategoryId getId() {
        return id;
    }

    public void setId(BookCategoryId id) {
        this.id = id;
    }

    public Boolean getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
}
