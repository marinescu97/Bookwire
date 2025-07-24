package com.store.Bookwire.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto user for placing an order")
public class OrderItemDto implements Serializable {
    @Schema(description = "Id of the book", example = "3")
    private Long bookId;
    @Schema(description = "The number of items", example = "1")
    private Integer quantity;
}
