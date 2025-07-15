package com.store.Bookwire.specifications.builder;

import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build();
}