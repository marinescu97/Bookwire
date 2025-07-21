package com.store.Bookwire.models.dtos.view;

import com.store.Bookwire.models.Category;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "DTO used to display a book", oneOf = {BookAdminDto.class, BookCustomerDto.class})
public class BookViewDto implements Serializable {
    @Schema(description = "The title of the book")
    private String title;
    @Schema(description = "The author of the book")
    private String author;
    @Schema(description = "The category of the book")
    private Category category;
    @Schema(description = "The isbn of the book")
    private String isbn;
    @Schema(description = "The year of publication of the book")
    private String publicationYear;
    @Schema(description = "The number of pages of the book")
    private String numberOfPages;
    @Schema(description = "The price of the book")
    private BigDecimal price;
}