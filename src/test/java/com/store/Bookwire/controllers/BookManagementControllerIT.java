package com.store.Bookwire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class BookManagementControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private BookRequestDTO testDto;
    private String stringDto;
    private Book testBook;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        testDto = BookRequestDTO.builder()
                .title("Test book")
                .author("Test author")
                .category(Category.BIOGRAPHY)
                .isbn("1234567890123")
                .publicationYear("2013")
                .numberOfPages("300")
                .price(new BigDecimal("12.99"))
                .quantity(10)
                .build();

        stringDto = """
                {
                    "title": "",
                    "author": "Author",
                    "category": "BIOGRAPHY",
                    "isbn": "123456789",
                    "publicationYear": "208",
                    "numberOfPages": "-1",
                    "price": -1.00,
                    "quantity": -1
                }
                """;
        testBook = bookMapper.toEntity(testDto);
    }

    @Test
    void save_validData_shouldSaveBook() throws Exception{
        mockMvc.perform(post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value(testDto.getTitle()))
                .andExpect(jsonPath("$.author").value(testDto.getAuthor()))
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.updatedDate").exists());

        Book savedBook = repository.findAll().getFirst();

        assertThat(savedBook.getId()).isNotNull();
        assertEquals(testBook.getTitle(), savedBook.getTitle());
        assertThat(savedBook.getCreatedDate()).isNotNull();
        assertThat(savedBook.getUpdatedDate()).isNotNull();
    }

    @Test
    void save_invalidData_shouldReturnBadRequest_withErrorMessages() throws Exception{
        mockMvc.perform(post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringDto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank."))
                .andExpect(jsonPath("$.isbn").value("ISBN must be exactly 13 characters long."))
                .andExpect(jsonPath("$.publicationYear").value("Publication year must be a 4-digit number."))
                .andExpect(jsonPath("$.numberOfPages").value("Number of pages must be between 1 and 4 digits."))
                .andExpect(jsonPath("$.price").value("Price must be at least 0.00."))
                .andExpect(jsonPath("$.quantity").value("Quantity must be a positive number."));

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void save_invalidCategory_shouldReturnBadRequest_withErrorMessage() throws Exception{
        stringDto = """
                {
                    "title": "",
                    "author": "Author",
                    "category": "IT",
                    "isbn": "123456789",
                    "publicationYear": "208",
                    "numberOfPages": "-1",
                    "price": -1.00,
                    "quantity": -1
                }
                """;

        mockMvc.perform(post("/api/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringDto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.category").value("Invalid value 'IT' for field 'category'. Allowed values are: " + Arrays.toString(Category.values())));

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_existingBook_shouldDeleteBook() throws Exception{
        repository.save(testBook);

        mockMvc.perform(delete("/api/books/" + testBook.getId()))
                .andExpect(status().isNoContent());

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingBook_shouldReturnNotFound() throws Exception{
        repository.save(testBook);

        long id = 100L;
        mockMvc.perform(delete("/api/books/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
    }

    @Test
    void updateBook_existingBook_shouldUpdateBook() throws Exception{
        repository.save(testBook);

        testDto.setTitle("Updated title");

        LocalDateTime oldUpdatedDate = testBook.getUpdatedDate();

        mockMvc.perform(patch("/api/books/" + testBook.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.title").value(testDto.getTitle()),
                        jsonPath("$.author").value(testBook.getAuthor())
                );

        entityManager.flush();
        entityManager.clear();

        Book updatedBook = repository.findById(testBook.getId()).orElseThrow();


        assertEquals(testBook.getId(), updatedBook.getId());
        assertEquals(testDto.getTitle(), updatedBook.getTitle());
        assertEquals(testBook.getAuthor(), updatedBook.getAuthor());
        assertEquals(testBook.getCreatedDate(), updatedBook.getCreatedDate());
        assertNotEquals(oldUpdatedDate, updatedBook.getUpdatedDate());
    }

    @Test
    void updateById_nonExistingBook_shouldReturnNotFound() throws Exception{
        repository.save(testBook);

        long id = 100L;

        mockMvc.perform(patch("/api/books/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
    }

    @Test
    void updateById_invalidData_shouldReturnBadRequest_withErrorMessages() throws Exception{
        repository.save(testBook);

        mockMvc.perform(patch("/api/books/" + testBook.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringDto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title cannot be blank."))
                .andExpect(jsonPath("$.isbn").value("ISBN must be exactly 13 characters long."))
                .andExpect(jsonPath("$.publicationYear").value("Publication year must be a 4-digit number."))
                .andExpect(jsonPath("$.numberOfPages").value("Number of pages must be between 1 and 4 digits."))
                .andExpect(jsonPath("$.price").value("Price must be at least 0.00."))
                .andExpect(jsonPath("$.quantity").value("Quantity must be a positive number."));

        Book book = repository.findById(testBook.getId()).orElseThrow();

        assertEquals(book, testBook);
    }

    @Test
    void updateById_invalidCategory_shouldReturnBadRequest_withErrorMessage() throws Exception{
        repository.save(testBook);

        stringDto = """
                {
                    "title": "",
                    "author": "Author",
                    "category": "IT",
                    "isbn": "123456789",
                    "publicationYear": "208",
                    "numberOfPages": "-1",
                    "price": -1.00,
                    "quantity": -1
                }
                """;

        mockMvc.perform(patch("/api/books/" + testBook.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stringDto))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.category").value("Invalid value 'IT' for field 'category'. Allowed values are: " + Arrays.toString(Category.values())));

        Book book = repository.findById(testBook.getId()).orElseThrow();

        assertEquals(testBook, book);
    }
}