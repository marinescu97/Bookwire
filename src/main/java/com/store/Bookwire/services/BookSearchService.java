package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.BookDto;

import java.util.List;
import java.util.Optional;

public interface BookSearchService {
    List<BookDto> getAll(int page, int size, String sortBy, String direction);
    List<BookDto> searchBooks(String value, int page, int size, String sortBy, String direction);
    List<BookDto> filterBooks(List<String> categories, Double minPrice, Double maxPrice, Integer minPages, Integer maxPages, int page, int size, String sortBy, String direction);
    Optional<BookDto> getBook(Long id);
}
