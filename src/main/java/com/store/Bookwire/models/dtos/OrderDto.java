package com.store.Bookwire.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto used for placing an order")
public class OrderDto implements Serializable {
    @Schema(description = "List of order items")
    private List<OrderItemDto> items;
}
