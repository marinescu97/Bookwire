package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.view.BookViewDto;

import java.util.List;
import java.util.Optional;

public interface BookSearchService {
    List<BookViewDto> getAll(int page, int size, String sortBy, String direction);
    List<BookViewDto> searchBooks(String value, int page, int size, String sortBy, String direction);
    List<BookViewDto> filterBooks(List<String> categories, Double minPrice, Double maxPrice, Integer minPages, Integer maxPages, int page, int size, String sortBy, String direction);
    Optional<BookViewDto> getBook(Long id);
}
