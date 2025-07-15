package com.store.Bookwire.services.impl;

import com.store.Bookwire.mappers.BookMapper;
import com.store.Bookwire.models.dtos.BookRequestDTO;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.repositories.BookRepository;
import com.store.Bookwire.services.BookSearchService;
import com.store.Bookwire.specifications.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
public class BookSearchServiceImpl implements BookSearchService {
    private final BookRepository repository;
    private final BookMapper mapper;
    private final BookSpecification bookSpecification;

    private final Set<String> SORT_FIELDS = Set.of("title", "price", "numberOfPages");

    @Override
    public List<BookRequestDTO> getAll(int page, int size, String sortBy, String direction){
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return repository.findAll(pageable)
                .getContent()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookRequestDTO> searchBooks(String value, int page, int size, String sortBy, String direction){
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        Specification<Book> spec = bookSpecification.titleAuthorIsbnContains(value);
        Page<Book> result = repository.findAll(spec, pageable);
        return result.getContent()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<BookRequestDTO> filterBooks(List<String> categories, Double minPrice, Double maxPrice, Integer minPages, Integer maxPages, int page, int size, String sortBy, String direction) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);

        Specification<Book> spec = bookSpecification
                        .hasCategory(categories)
                        .and(bookSpecification.priceBetween(minPrice, maxPrice))
                        .and(bookSpecification.pagesBetween(minPages, maxPages));

        Page<Book> result = repository.findAll(spec, pageable);
        return result.getContent().stream()
                .map(mapper::toDto)
                .toList();
    }

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        String validatedSortBy = SORT_FIELDS.contains(sortBy) ? sortBy : "title";

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(validatedSortBy).descending()
                : Sort.by(validatedSortBy).ascending();

        return PageRequest.of(page, size, sort);
    }
}
