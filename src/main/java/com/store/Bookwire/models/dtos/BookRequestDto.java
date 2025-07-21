package com.store.Bookwire.models.dtos;

import com.store.Bookwire.models.Category;
import com.store.Bookwire.models.entities.Book;
import com.store.Bookwire.validators.Unique;
import jakarta.validation.constraints.*;
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
@Unique.List({
        @Unique(entity = Book.class, fieldName = "title", message = "Title already exists."),
        @Unique(entity = Book.class, fieldName = "isbn", message = "ISBN already exists.")
})
public class BookRequestDto implements Serializable {
    @NotBlank(message = "Title cannot be blank.")
    @Size(max = 100, message = "Title must be at most 100 characters long.")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 50, message = "Author must be at most 50 characters long")
    private String author;

    @NotNull(message = "Category is required.")
    private Category category;

    @NotBlank(message = "ISBN cannot be blank.")
    @Size(min = 13, max = 13, message = "ISBN must be exactly 13 characters long.")
    private String isbn;

    @NotBlank(message = "Publication year cannot be blank.")
    @Pattern(regexp = "\\d{4}", message = "Publication year must be a 4-digit number.")
    private String publicationYear;

    @NotBlank(message = "Number of pages cannot be blank.")
    @Pattern(regexp = "\\d{1,4}", message = "Number of pages must be between 1 and 4 digits.")
    private String numberOfPages;

    @NotNull(message = "Price cannot be null.")
    @DecimalMin(value = "0.00", message = "Price must be at least 0.00.")
    @DecimalMax(value = "999.99", message = "Price must not exceed 999.99.")
    private BigDecimal price;

    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 0, message = "Quantity must be a positive number.")
    private Integer quantity;
}
