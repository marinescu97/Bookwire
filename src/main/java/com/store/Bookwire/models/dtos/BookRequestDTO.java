package com.store.Bookwire.models.dtos;

import com.store.Bookwire.models.Category;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookRequestDTO {
    private String title;
    private String author;
    private Category category;
    private String isbn;
    private String publicationYear;
    private Integer numberOfPages;
    private BigDecimal price;
    private Integer quantity;
}
