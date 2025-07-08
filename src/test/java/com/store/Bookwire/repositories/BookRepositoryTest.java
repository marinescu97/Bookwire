package com.store.Bookwire.repositories;

import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.entities.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {
    @Autowired
    private BookRepository repository;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = Book.builder()
                .title("Test book")
                .author("Test author")
                .category(Category.BIOGRAPHY)
                .isbn("1234567893653")
                .publicationYear("2012")
                .numberOfPages(320)
                .price(new BigDecimal("23.99"))
                .quantity(10)
                .build();

        testBook = repository.save(testBook);
    }

    @Test
    void testSaveBook_idAndTimestampsNotNull() {
        assertThat(testBook.getId()).isNotNull();
        assertThat(testBook.getCreatedDate()).isNotNull();
        assertThat(testBook.getUpdatedDate()).isNotNull();
    }

    @Test
    void testFindById_shouldFindBook() {
        Optional<Book> foundBook = repository.findById(testBook.getId());

        assertThat(foundBook).isPresent();
        assertEquals(testBook.getTitle(), foundBook.get().getTitle());
        assertEquals(testBook.getAuthor(), foundBook.get().getAuthor());
    }
}