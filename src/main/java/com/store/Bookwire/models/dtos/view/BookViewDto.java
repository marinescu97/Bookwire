package com.store.Bookwire.models.dtos.view;

import com.store.Bookwire.models.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BookViewDto implements Serializable {
    private String title;
    private String author;
    private Category category;
    private String isbn;
    private String publicationYear;
    private String numberOfPages;
    private BigDecimal price;
}