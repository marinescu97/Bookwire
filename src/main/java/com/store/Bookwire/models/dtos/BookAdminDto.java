package com.store.Bookwire.models.dtos;

import com.store.Bookwire.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used for creating or displaying a book for an admin")
public class BookAdminDto extends BookDto {
    @NotNull(message = "Quantity cannot be null.")
    @Min(value = 0, message = "Quantity must be a positive number.")
    @Schema(description = "Number of copies available in stock", example = "50")
    private Integer quantity;

    public BookAdminDto(String title, String author, Category category, String isbn, String publicationYear, String numberOfPages, BigDecimal price, Integer quantity) {
        super(title, author, category, isbn, publicationYear, numberOfPages, price);
        this.quantity = quantity;
    }
}