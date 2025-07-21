package com.store.Bookwire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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

    protected BookRequestDto testDto;
    protected String stringDto;
    protected Book testBook;

    void initTestDto() {
        testDto = BookRequestDto.builder()
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

    @Nested
    class DatabaseHasNoData {
        @BeforeEach
        void setUp() {
            repository.deleteAll();
            initTestDto();
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void save_validData_shouldSaveBook() throws Exception {
            mockMvc.perform(post("/api/admin/books")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(testDto.getTitle()))
                    .andExpect(jsonPath("$.author").value(testDto.getAuthor()));

            Book savedBook = repository.findAll().getFirst();

            assertThat(savedBook.getId()).isNotNull();
            assertEquals(testBook.getTitle(), savedBook.getTitle());
            assertThat(savedBook.getCreatedDate()).isNotNull();
            assertThat(savedBook.getUpdatedDate()).isNotNull();
        }

        @Test
        @WithMockUser(username = "customer", roles = {"CUSTOMER"})
        void save_invalidUser_shouldNotSaveBook() throws Exception {
            mockMvc.perform(post("/api/admin/books")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void save_invalidData_shouldReturnBadRequest_withErrorMessages() throws Exception {
            mockMvc.perform(post("/api/admin/books")
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
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void save_invalidCategory_shouldReturnBadRequest_withErrorMessage() throws Exception {
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

            mockMvc.perform(post("/api/admin/books")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringDto))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.category").value("Invalid value 'IT' for field 'category'. Allowed values are: " + Arrays.toString(Category.values())));

            assertThat(repository.findAll()).isEmpty();
        }
    }

    @Nested
    class WhenDatabaseHasSavedBook {
        @BeforeEach
        void setUp() {
            repository.deleteAll();
            initTestDto();
            testBook = repository.save(bookMapper.toEntity(testDto));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void save_duplicateData_shouldReturnBadRequest_withErrorMessages() throws Exception {
            mockMvc.perform(post("/api/admin/books")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("Title already exists."))
                    .andExpect(jsonPath("$.isbn").value("ISBN already exists."));

            assertThat(repository.findAll()).size().isEqualTo(1);
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void deleteById_existingBook_shouldDeleteBook() throws Exception {
            mockMvc.perform(delete("/api/admin/books/" + testBook.getId()))
                    .andExpect(status().isNoContent());

            assertThat(repository.findAll()).isEmpty();
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void deleteById_nonExistingBook_shouldReturnNotFound() throws Exception {
            long id = 100L;
            mockMvc.perform(delete("/api/admin/books/" + id)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateBook_existingBook_shouldUpdateBook() throws Exception {
            testDto.setTitle("Updated title");

            LocalDateTime oldUpdatedDate = testBook.getUpdatedDate();

            mockMvc.perform(put("/api/admin/books/" + testBook.getId())
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
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateById_nonExistingBook_shouldReturnNotFound() throws Exception {
            long id = 100L;

            testDto.setTitle("Another title");
            testDto.setIsbn("8364790354723");

            mockMvc.perform(put("/api/admin/books/" + id)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Book with id " + id + " not found."));
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateById_invalidData_shouldReturnBadRequest_withErrorMessages() throws Exception {
            mockMvc.perform(put("/api/admin/books/" + testBook.getId())
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
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateById_invalidCategory_shouldReturnBadRequest_withErrorMessage() throws Exception {
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

            mockMvc.perform(put("/api/admin/books/" + testBook.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(stringDto))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.category").value("Invalid value 'IT' for field 'category'. Allowed values are: " + Arrays.toString(Category.values())));

            Book book = repository.findById(testBook.getId()).orElseThrow();

            assertEquals(testBook, book);
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateById_duplicateData_andAnotherId_shouldReturnBadRequest_withErrorMessages() throws Exception {
            testDto.setTitle("Test duplicate title");
            testDto.setIsbn("8364790354723");
            Book anotherBook = repository.save(bookMapper.toEntity(testDto));

            mockMvc.perform(put("/api/admin/books/" + testBook.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.title").value("Title already exists."))
                    .andExpect(jsonPath("$.isbn").value("ISBN already exists."));

            Book book = repository.findById(testBook.getId()).orElseThrow();

            assertEquals(testBook.getTitle(), book.getTitle());
            assertEquals(testBook.getIsbn(), book.getIsbn());
        }

        @Test
        @WithMockUser(username = "admin", roles = {"ADMIN"})
        void updateById_duplicateData_andSameId_shouldUpdateBook() throws Exception {
            testDto.setQuantity(10);

            mockMvc.perform(put("/api/admin/books/" + testBook.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(testDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title").value(testDto.getTitle()))
                    .andExpect(jsonPath("$.isbn").value(testDto.getIsbn()))
                    .andExpect(jsonPath("$.quantity").value(testDto.getQuantity()));

            Book book = repository.findById(testBook.getId()).orElseThrow();

            assertEquals(testDto.getTitle(), book.getTitle());
            assertEquals(testDto.getIsbn(), book.getIsbn());
            assertEquals(testDto.getQuantity(), book.getQuantity());
        }
    }
}