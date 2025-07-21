package com.store.Bookwire.models.dtos.view;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used for displaying a book for a customer or guest")
public class BookCustomerDto extends BookViewDto {
    @Schema(description = "A text displayed according to quantity", example = "In stock/Out of stock")
    private String stock;
}