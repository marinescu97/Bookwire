package com.store.Bookwire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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

    private static BookRequestDTO testDto;
    private Book testBook;

    @BeforeAll
    static void beforeAll() {
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
    }

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        testBook = repository.save(bookMapper.toEntity(testDto));
    }

    @Test
    void save_validData_shouldSaveBook() throws Exception{
        repository.delete(testBook);

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
    void deleteById_existingBook_shouldDeleteBook() throws Exception{
        mockMvc.perform(delete("/api/books/" + testBook.getId()))
                .andExpect(status().isNoContent());

        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void deleteById_nonExistingBook_shouldReturnNotFound() throws Exception{
        long id = 100L;
        mockMvc.perform(delete("/api/books/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
    }

    @Test
    void updateBook_existingBook_shouldUpdateBook() throws Exception{
        testDto.setTitle("Updated title");
        testDto.setAuthor(null);

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
        long id = 100L;

        mockMvc.perform(patch("/api/books/" + id)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
    }
}