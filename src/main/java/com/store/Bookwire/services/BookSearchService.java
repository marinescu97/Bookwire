package com.store.Bookwire.services;

import com.store.Bookwire.models.dtos.BookRequestDTO;

import java.util.List;

public interface BookSearchService {
    List<BookRequestDTO> getAll(int page, int size, String sortBy, String direction);
    List<BookRequestDTO> searchBooks(String value, int page, int size, String sortBy, String direction);
    List<BookRequestDTO> filterBooks(List<String> categories, Double minPrice, Double maxPrice, Integer minPages, Integer maxPages, int page, int size, String sortBy, String direction);
}
