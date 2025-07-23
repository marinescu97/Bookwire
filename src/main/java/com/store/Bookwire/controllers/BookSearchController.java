package com.store.Bookwire.controllers;

import com.store.Bookwire.models.dtos.BookDto;
import com.store.Bookwire.services.BookSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book search")
public class BookSearchController {

    private final BookSearchService service;

    @Autowired
    public BookSearchController(BookSearchService service) {
        this.service = service;
    }

    @Operation(summary = "Display book by id", operationId = "b_displayBook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Display book"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable Long id) {
        return service.getBook(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Display all books", operationId = "a_displayAll")
    @ApiResponse(responseCode = "200", description = "List of all books")
    @GetMapping
    public ResponseEntity<List<BookDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
                    Authentication auth) {
        List<BookDto> allBooks = service.getAll(page, size, sortBy, direction);
        return ResponseEntity.ok(allBooks);
    }

    @Operation(summary = "Search books by keyword",
                description = "Returns a list of books where the given keyword matches any searchable attribute, such as title, author, or isbn",
                operationId = "c_search")
    @ApiResponse(responseCode = "200", description = "List of found books")
    @GetMapping("/search/{value}")
    public ResponseEntity<List<BookDto>> searchBooks(
            @PathVariable String value,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        List<BookDto> books = service.searchBooks(value, page, size, sortBy, direction);
        return ResponseEntity.ok(books);
    }

    @Operation(
            summary = "Filter books based on multiple criteria",
            description = "Returns a paginated list of books filtered by category, price range, page count range, and sorted by a specific field.",
            operationId = "d_filter"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books filtered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters"),
    })
    @GetMapping("/filter")
    public List<BookDto> filterBooks(
            @Parameter(description = "List of book categories to filter by", example = "PROGRAMMING, HISTORY")
            @RequestParam(required = false) List<String> categories,

            @Parameter(description = "Minimum price of the book", example = "20.0")
            @RequestParam(required = false) Double minPrice,

            @Parameter(description = "Maximum price of the book", example = "50.99")
            @RequestParam(required = false) Double maxPrice,

            @Parameter(description = "Minimum number of pages", example = "100")
            @RequestParam(required = false) Integer minPages,

            @Parameter(description = "Maximum number of pages", example = "300")
            @RequestParam(required = false) Integer maxPages,

            @Parameter(description = "Page number for pagination", example = "2")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Field to sort by (e.g., title, price, pages)", example = "title")
            @RequestParam(defaultValue = "title") String sortBy,

            @Parameter(description = "Sort direction: 'asc' or 'desc'", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {
        return service.filterBooks(categories, minPrice, maxPrice, minPages, maxPages, page, size, sortBy, direction);
    }
}

