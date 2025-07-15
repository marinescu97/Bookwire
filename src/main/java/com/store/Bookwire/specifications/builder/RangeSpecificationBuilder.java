package com.store.Bookwire.specifications.builder;

import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class RangeSpecificationBuilder<T, N extends Number & Comparable<N>> implements SpecificationBuilder<T> {
    private final String field;
    private final N min;
    private final N max;

    public RangeSpecificationBuilder(String field, N min, N max) {
        this.field = field;
        this.min = min;
        this.max = max;
    }

    @Override
    public Specification<T> build() {
        return (root, query, cb) -> {
            if (min == null && max == null) {
                return cb.conjunction();
            }

            Path<N> path = root.get(field);

            if (min != null && max != null) {
                return cb.between(path, min, max);
            } else if (min != null) {
                return cb.greaterThanOrEqualTo(path, min);
            } else {
                return cb.lessThanOrEqualTo(path, max);
            }
        };
    }
}

