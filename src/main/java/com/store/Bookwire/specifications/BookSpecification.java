package com.store.Bookwire.specifications;

import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.specifications.builder.FieldEqualsSpecificationBuilder;
import com.store.Bookwire.specifications.builder.RangeSpecificationBuilder;
import com.store.Bookwire.specifications.builder.SearchSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookSpecification {
    public Specification<Book> titleAuthorIsbnContains(String value) {
        return new SearchSpecificationBuilder<Book>(value, List.of("title", "author", "isbn")).build();
    }

    public Specification<Book> hasCategory(List<String> categories) {
        return new FieldEqualsSpecificationBuilder<Book>(List.of("category"), categories).build();
    }

    public Specification<Book> priceBetween(Double min, Double max) {
        return new RangeSpecificationBuilder<Book, Double>("price", min, max).build();
    }

    public Specification<Book> pagesBetween(Integer min, Integer max) {
        return new RangeSpecificationBuilder<Book, Integer>("numberOfPages", min, max).build();
    }
}

