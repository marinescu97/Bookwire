package com.store.Bookwire.specifications;

import com.store.Bookwire.specifications.builder.FieldEqualsSpecificationBuilder;
import com.store.Bookwire.specifications.builder.RangeSpecificationBuilder;
import com.store.Bookwire.specifications.builder.SearchSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class EntitySpecification<T> {
    public Specification<T> search(String value, List<String> fields) {
        return new SearchSpecificationBuilder<T>(value, fields).build();
    }

    public Specification<T> fieldEquals(List<String> fields, List<String> values) {
        return new FieldEqualsSpecificationBuilder<T>(fields, values).build();
    }

    public <N extends Number & Comparable<N>> Specification<T> isBetween(String field, N min, N max) {
        return new RangeSpecificationBuilder<T, N>(field, min, max).build();
    }
}

