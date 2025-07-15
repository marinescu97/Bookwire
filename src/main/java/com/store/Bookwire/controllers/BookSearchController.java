package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.services.BookSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
public class BookSearchController {

    private final BookSearchService service;

    @Autowired
    public BookSearchController(BookSearchService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<BookRequestDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        List<BookRequestDTO> allBooks = service.getAll(page, size, sortBy, direction);
        return ResponseEntity.ok(allBooks);
    }

    @GetMapping("/search/{value}")
    public ResponseEntity<List<BookRequestDTO>> searchBooks(
            @PathVariable String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        List<BookRequestDTO> books = service.searchBooks(value, page, size, sortBy, direction);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/filter")
    public List<BookRequestDTO> filterBooks(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minPages,
            @RequestParam(required = false) Integer maxPages,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return service.filterBooks(categories, minPrice, maxPrice, minPages, maxPages, page, size, sortBy, direction);
    }
}

