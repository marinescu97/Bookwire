package com.store.Bookwire.services;

import com.store.Bookwire.exceptions.ResourceNotFoundException;
import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.dtos.BookRequestDto;
import com.store.Bookwire.models.dtos.BookUpdateDto;
import com.store.Bookwire.models.dtos.view.BookAdminDto;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import com.store.Bookwire.services.impl.BookManagementServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookManagementServiceImplTest {
    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookManagementServiceImpl service;

    @Mock
    private BookMapper mapper;

    private static BookRequestDto testDto;
    private static BookUpdateDto updateDTO;
    private Book testBook;
    private static BookAdminDto testBookAdminDto;

    @BeforeAll
    static void beforeAll() {
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

        updateDTO = BookUpdateDto.builder().build();

        testBookAdminDto = BookAdminDto.builder()
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
        testBook = new Book();
    }

    @Test
    void save_validData_shouldSaveBook() {
        when(mapper.toEntity(testDto)).thenReturn(testBook);
        when(repository.save(any(Book.class))).thenReturn(testBook);
        when(mapper.toAdminDto(testBook)).thenReturn(testBookAdminDto);

        BookAdminDto savedBook = service.save(testDto);

        assertNotNull(savedBook);
        assertEquals(testBookAdminDto.getTitle(), savedBook.getTitle());
        assertEquals(testBookAdminDto.getAuthor(), savedBook.getAuthor());

        verify(mapper).toEntity(testDto);
        verify(repository).save(any(Book.class));
        verify(mapper).toAdminDto(testBook);
    }

    @Test
    void deleteById_existingBook_shouldDeleteBook() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(testBook));

        service.deleteById(id);

        verify(repository).findById(id);
        verify(repository).delete(testBook);
    }

    @Test
    void deleteById_nonExistingBook_shouldThrowResourceNotFoundException() {
        Long bookId = 100L;

        when(repository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteById(bookId));

        String expectedMessage = String.format("Book with id %d not found.", bookId);
        assertEquals(expectedMessage, exception.getMessage());

        verify(repository, never()).deleteById(bookId);
    }

    @Test
    void updateById_existingBook_shouldUpdateBook() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.of(testBook));
        doNothing().when(mapper).updateFromDto(updateDTO, testBook);

        when(repository.save(testBook)).thenReturn(testBook);

        when(mapper.toAdminDto(testBook)).thenReturn(testBookAdminDto);

        BookAdminDto result = service.updateById(id, updateDTO);

        assertEquals(testBookAdminDto, result);
        verify(mapper).updateFromDto(updateDTO, testBook);
        verify(repository).save(testBook);
        verify(mapper).toAdminDto(testBook);
    }

    @Test
    void updateById_nonExistingBook_shouldThrowResourceNotFoundException() {
        Long invalidId = 100L;
        when(repository.findById(invalidId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> service.updateById(invalidId, updateDTO));

        String expectedMessage = String.format("Book with id %d not found.", invalidId);
        assertEquals(expectedMessage, exception.getMessage());
        verify(repository).findById(invalidId);
    }
}