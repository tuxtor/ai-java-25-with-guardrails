-- Author table
CREATE TABLE AUTHOR (
    author_id  NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    given_name  VARCHAR2(255),
    family_name VARCHAR2(255),
    display_name VARCHAR2(255),
    birth_date  DATE,
    death_date  DATE,
    biography   CLOB,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Book table
CREATE TABLE BOOK (
    book_id          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    isbn             VARCHAR2(50) UNIQUE,
    title            VARCHAR2(500),
    subtitle         VARCHAR2(500),
    description      CLOB,
    publication_date DATE,
    edition          VARCHAR2(50),
    language_code    VARCHAR2(10),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Book-Author associative table (many-to-many)
CREATE TABLE BOOK_AUTHOR (
    book_id           NUMBER NOT NULL,
    author_id         NUMBER NOT NULL,
    author_order      NUMBER,
    contribution_role VARCHAR2(100),
    credited_name     VARCHAR2(255),
    CONSTRAINT pk_book_author  PRIMARY KEY (book_id, author_id),
    CONSTRAINT fk_ba_book      FOREIGN KEY (book_id)   REFERENCES BOOK(book_id),
    CONSTRAINT fk_ba_author    FOREIGN KEY (author_id) REFERENCES AUTHOR(author_id)
);

-- Category / classification taxonomy
CREATE TABLE CATEGORY (
    category_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    parent_category_id NUMBER,
    code               VARCHAR2(100) UNIQUE,
    name               VARCHAR2(255),
    description        CLOB,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_cat_parent FOREIGN KEY (parent_category_id) REFERENCES CATEGORY(category_id)
);

-- Book-Category associative table (many-to-many)
CREATE TABLE BOOK_CATEGORY (
    book_id     NUMBER       NOT NULL,
    category_id NUMBER       NOT NULL,
    -- is_primary: 0 = false (not primary category), 1 = true (primary category)
    is_primary  NUMBER(1, 0) DEFAULT 0,
    CONSTRAINT pk_book_category PRIMARY KEY (book_id, category_id),
    CONSTRAINT fk_bc_book       FOREIGN KEY (book_id)     REFERENCES BOOK(book_id),
    CONSTRAINT fk_bc_category   FOREIGN KEY (category_id) REFERENCES CATEGORY(category_id)
);
