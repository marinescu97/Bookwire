package com.store.Bookwire.specifications.builder;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SearchSpecificationBuilder<T> implements SpecificationBuilder<T> {
    private final String value;
    private final List<String> fields;

    public SearchSpecificationBuilder(String value, List<String> fields) {
        this.value = value;
        this.fields = fields;
    }

    @Override
    public Specification<T> build() {
        return (root, query, cb) -> {
            if (value == null || value.trim().isEmpty() || fields == null || fields.isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + value.toLowerCase() + "%";
            List<Predicate> predicates = new ArrayList<>();

            for (String field : fields) {
                predicates.add(cb.like(cb.lower(root.get(field).as(String.class)), pattern));
            }

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}

