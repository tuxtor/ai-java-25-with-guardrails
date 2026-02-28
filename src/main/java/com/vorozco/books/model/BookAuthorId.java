package com.vorozco.books.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for the BOOK_AUTHOR associative table.
 */
@Embeddable
public class BookAuthorId implements Serializable {

    @Column(name = "BOOK_ID")
    private Long bookId;

    @Column(name = "AUTHOR_ID")
    private Long authorId;

    public BookAuthorId() {
    }

    public BookAuthorId(Long bookId, Long authorId) {
        this.bookId = bookId;
        this.authorId = authorId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookAuthorId that)) return false;
        return Objects.equals(bookId, that.bookId) && Objects.equals(authorId, that.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, authorId);
    }
}
