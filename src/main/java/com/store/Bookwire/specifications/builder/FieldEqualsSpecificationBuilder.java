package com.store.Bookwire.specifications.builder;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class FieldEqualsSpecificationBuilder<T> implements SpecificationBuilder<T> {
    private final List<String> fields;
    private final List<String> values;

    public FieldEqualsSpecificationBuilder(List<String> fields, List<String> values) {
        this.fields = fields;
        this.values = values;
    }

    @Override
    public Specification<T> build() {
        return (root, query, cb) -> {
            if (fields == null || fields.isEmpty() || values == null || values.isEmpty()) {
                return cb.conjunction();
            }

            List<Predicate> fieldPredicates = new ArrayList<>();

            for (String field : fields) {
                List<Predicate> valuePredicates = new ArrayList<>();

                for (String value : values) {
                    if (value != null && !value.trim().isEmpty()) {
                        valuePredicates.add(
                                cb.equal(cb.lower(root.get(field).as(String.class)), value.trim().toLowerCase())
                        );
                    }
                }

                if (!valuePredicates.isEmpty()) {
                    fieldPredicates.add(cb.or(valuePredicates.toArray(new Predicate[0])));
                }
            }

            return cb.or(fieldPredicates.toArray(new Predicate[0]));
        };
    }
}

