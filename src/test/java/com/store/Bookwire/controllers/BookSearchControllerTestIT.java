package com.store.Bookwire.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.UserRole;
import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.view.BookCustomerDto;
import com.store.Bookwire.models.entities.User;
import com.store.Bookwire.repositories.BookRepository;
import com.store.Bookwire.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
class BookSearchControllerTestIT {
    @Autowired private MockMvc mockMvc;
    @Autowired private BookRepository repository;
    @Autowired private BookMapper mapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    private final String SEARCH_URI = "/api/books/search";

    private List<BookRequestDto> testBooks;
    private String adminToken;
    private String customerToken;

    @BeforeEach
    void setUpBooks() {
        repository.deleteAll();

        testBooks = List.of(
                new BookRequestDto("Clean Architecture", "Robert C. Martin", Category.SOFTWARE_DEVELOPMENT, "9780134494166", "2017", "432", new BigDecimal("34.99"), 6),
                new BookRequestDto("Agile Software Development, Principles, Patterns, and Practices", "Robert C. Martin", Category.SOFTWARE_DEVELOPMENT, "9780135974445", "2002", "552", new BigDecimal("37.50"), 5),
                new BookRequestDto("Thinking, Fast and Slow", "Daniel Kahneman", Category.NON_FICTION, "9780374533557", "2013", "512", new BigDecimal("18.99"), 7),
                new BookRequestDto("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", Category.HISTORY, "9780062316097", "2015", "464", new BigDecimal("22.00"), 9),
                new BookRequestDto("The Design of Everyday Things", "Don Norman", Category.TECHNOLOGY, "9780465050659", "2013", "368", new BigDecimal("20.00"), 8)
        );

        repository.saveAll(testBooks.stream()
                .map(mapper::toEntity)
                .toList());
    }

    @BeforeEach
    void setUpCustomers(){
        User customer = new User();
        customer.setUsername("testCustomer");
        customer.setEmail("customer@email.com");
        customer.setPassword(passwordEncoder.encode("password"));
        customer.setRole(UserRole.CUSTOMER);

        userRepository.save(customer);
    }

    @Test
    void getAll_asGuest_firstPage_shouldReturnAllBooks() throws Exception{
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(testBooks.size()));

        List<BookCustomerDto> foundBooks = repository.findAll().stream()
                .map(mapper::toCustomerDto)
                .toList();
        assertEquals(testBooks.size(), foundBooks.size());
        assertEquals("In stock", foundBooks.getFirst().getStock());
    }

    @Test
    void getAll_asGuest_secondPage_shouldReturnEmptyList() throws Exception{
        mockMvc.perform(get("/api/books?page=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAll_asGuest_sortByAuthorDesc_shouldReturnAllBooks_sortedByAuthor_descendingOrder() throws Exception{
        testBooks = testBooks.stream()
                        .sorted(Comparator.comparing(BookRequestDto::getPrice).reversed())
                        .collect(Collectors.toList());
        mockMvc.perform(get("/api/books?sortBy=price&direction=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(testBooks.size()))
                .andExpect(jsonPath("$[0].title").value(testBooks.getFirst().getTitle()))
                .andExpect(jsonPath("$[4].title").value(testBooks.getLast().getTitle()));
    }

    @Test
    void getAll_asGuest_customPageSize_shouldReturnLimitedBooks() throws Exception {
        mockMvc.perform(get("/api/books?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAll_asGuest_invalidSortField_shouldFallbackToDefault() throws Exception {
        testBooks = testBooks.stream()
                .sorted(Comparator.comparing(BookRequestDto::getTitle))
                .toList();

        mockMvc.perform(get("/api/books?sortBy=invalidField&direction=desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(testBooks.getLast().getTitle()));
    }

    @Test
    void searchBooks_asAdmin_byTitle_shouldReturnOneBook() throws Exception{
        authenticateAdmin();
        BookRequestDto book = testBooks.getFirst();

        mockMvc.perform(get(SEARCH_URI + "/clean")
                .header("Authorization", "Bearer " + adminToken))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$").isArray(),
                        jsonPath("$.length()").value(1),
                        jsonPath("$[0].title").value(book.getTitle()),
                        jsonPath("$[0].author").value(book.getAuthor()),
                        jsonPath("$[0].quantity").value(book.getQuantity())
                );
    }

    @Test
    void searchBooks_asCustomer_byAuthor_shouldReturnTwoBooks() throws Exception{
        authenticateCustomer();
        List<BookCustomerDto> books = repository.findAll().stream()
                        .filter(book -> book.getAuthor().equals("Robert C. Martin"))
                        .map(book -> mapper.toCustomerDto(book))
                        .sorted(Comparator.comparing(BookCustomerDto::getTitle))
                        .toList();

        mockMvc.perform(get(SEARCH_URI + "/robert")
                .header("Authorization", "Bearer " + customerToken))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(books.size()),
                        jsonPath("$[0].title").value(books.getFirst().getTitle()),
                        jsonPath("$[1].title").value(books.getLast().getTitle()),
                        jsonPath("$[0].stock").value("In stock")
                );
    }

    @Test
    void searchBooks_asGuest_caseInsensitive_shouldReturnResults() throws Exception {
        mockMvc.perform(get(SEARCH_URI + "/SaPienS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchBooks_asGuest_noResults_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get(SEARCH_URI + "/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void filterBooks_asGuest_bySingleCategory_shouldReturnExpectedBooks() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("categories", "SOFTWARE_DEVELOPMENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].category").value("SOFTWARE_DEVELOPMENT"));
    }

    @Test
    void filterBooks_asGuest_byMultipleCategories_shouldReturnExpectedBooks() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("categories", "SOFTWARE_DEVELOPMENT", "TECHNOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void filterBooks_asGuest_byPriceRange_shouldReturnExpectedBooks() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("minPrice", "20")
                        .param("maxPrice", "35"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void filterBooks_asGuest_byPagesRange_shouldReturnExpectedBooks() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("minPages", "500")
                        .param("maxPages", "600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void filterBooks_asGuest_combinedFilters_shouldReturnExpectedBooks() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("categories", "SOFTWARE_DEVELOPMENT")
                        .param("minPrice", "30")
                        .param("maxPrice", "40")
                        .param("minPages", "400")
                        .param("maxPages", "600"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void filterBooks_asGuest_withNoMatches_shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/api/books/filter")
                        .param("minPrice", "1000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    private void authenticateCustomer() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                    "username": "testCustomer",
                    "password": "password"
                    }
                    """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        customerToken = objectMapper.readTree(response).get("token").asText();
    }

    private void authenticateAdmin() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                    "username": "admin",
                    "password": "password"
                    }
                    """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        adminToken = objectMapper.readTree(response).get("token").asText();
    }
}