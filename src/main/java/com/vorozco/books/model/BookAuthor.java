package com.vorozco.books.model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * JPA entity representing the BOOK_AUTHOR associative table.
 * Resolves the many-to-many relationship between BOOK and AUTHOR.
 */
@Entity(name = "BookAuthor")
@Table(name = "BOOK_AUTHOR")
@Access(AccessType.FIELD)
public class BookAuthor {

    @EmbeddedId
    private BookAuthorId id;

    @Column(name = "AUTHOR_ORDER")
    private Integer authorOrder;

    @Column(name = "CONTRIBUTION_ROLE")
    private String contributionRole;

    @Column(name = "CREDITED_NAME")
    private String creditedName;

    public BookAuthor() {
    }

    public BookAuthor(BookAuthorId id) {
        this.id = id;
    }

    public BookAuthorId getId() {
        return id;
    }

    public void setId(BookAuthorId id) {
        this.id = id;
    }

    public Integer getAuthorOrder() {
        return authorOrder;
    }

    public void setAuthorOrder(Integer authorOrder) {
        this.authorOrder = authorOrder;
    }

    public String getContributionRole() {
        return contributionRole;
    }

    public void setContributionRole(String contributionRole) {
        this.contributionRole = contributionRole;
    }

    public String getCreditedName() {
        return creditedName;
    }

    public void setCreditedName(String creditedName) {
        this.creditedName = creditedName;
    }
}
