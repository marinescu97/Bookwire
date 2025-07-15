package com.store.Bookwire.specifications;

import com.store.Bookwire.models.entities.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookSpecification extends EntitySpecification<Book> {
    public Specification<Book> titleAuthorIsbnContains(String value) {
        return search(value, List.of("title", "author", "isbn"));
    }

    public Specification<Book> hasCategory(List<String> categories) {
        return fieldEquals(List.of("category"), categories);
    }

    public Specification<Book> priceBetween(Double min, Double max) {
        return isBetween("price", min, max);
    }

    public Specification<Book> pagesBetween(Integer min, Integer max) {
        return isBetween("numberOfPages", min, max);
    }
}

